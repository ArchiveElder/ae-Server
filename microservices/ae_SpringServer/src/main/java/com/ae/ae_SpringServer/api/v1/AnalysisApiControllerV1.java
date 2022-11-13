package com.ae.ae_SpringServer.api.v1;

import com.ae.ae_SpringServer.domain.User;
import com.ae.ae_SpringServer.dto.response.AnalysisDto;
import com.ae.ae_SpringServer.dto.response.v2.AnalysisResponseDtoV2;
import com.ae.ae_SpringServer.jpql.DateAnalysisDtoV2;
import com.ae.ae_SpringServer.service.AnalysisService;
import com.ae.ae_SpringServer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.valueOf;

@RestController
@RequiredArgsConstructor
public class AnalysisApiControllerV1 {
    private final AnalysisService analysisService;
    private final UserService userService;

    //5-1
    @GetMapping("api/analysis")
    public AnalysisResponseDtoV2 analysisResponse(@AuthenticationPrincipal String userId) {
        int status = 0;
        int ratioCarb, ratioPro, ratioFat, totalCarb, totalPro, totalFat;
        ratioCarb = ratioPro = ratioFat = totalCarb = totalPro = totalFat = 0;

        User user = userService.findOne(Long.valueOf(userId));
        List<DateAnalysisDtoV2> findRecords = analysisService.findRecords(Long.valueOf(userId));
        //받아온 기록이 7개일 경우 : 정상로직 : status = 1
        if(findRecords.size() == 7) {
            status = 1;
            List<AnalysisDto> collect = new ArrayList<>();

            for(DateAnalysisDtoV2 dateAnalysisDtoV2 : findRecords) {
                totalCarb += dateAnalysisDtoV2.getSumCarb();
                totalPro += dateAnalysisDtoV2.getSumPro();
                totalFat += dateAnalysisDtoV2.getSumFat();
                collect.add(new AnalysisDto(dateAnalysisDtoV2.getDate().substring(5,10), dateAnalysisDtoV2.getSumCal().intValue()));
            }
            int sum = totalCarb + totalPro + totalFat;
            ratioCarb = totalCarb * 100 / sum;
            ratioPro = totalPro * 100 / sum;
            ratioFat = totalFat * 100 / sum;
            return new AnalysisResponseDtoV2(status, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd.")),
                    String.valueOf((int) Math.round(Double.parseDouble(user.getRcal()))), String.valueOf((int) Math.round(Double.parseDouble(user.getRcarb()))),
                    String.valueOf((int) Math.round(Double.parseDouble(user.getRpro()))), String.valueOf((int) Math.round(Double.parseDouble(user.getRfat()))),
                    ratioCarb, ratioPro, ratioFat , totalCarb, totalPro, totalFat, collect);
        }
        //비정상 로직 status = 0
        else { return new AnalysisResponseDtoV2(status,LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd.")),
                String.valueOf((int) Math.round(Double.parseDouble(user.getRcal()))), String.valueOf((int) Math.round(Double.parseDouble(user.getRcarb()))),
                String.valueOf((int) Math.round(Double.parseDouble(user.getRpro()))), String.valueOf((int) Math.round(Double.parseDouble(user.getRfat()))),
                ratioCarb, ratioPro, ratioFat , totalCarb, totalPro, totalFat, null);}

    }
}
