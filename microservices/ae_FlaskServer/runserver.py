import pymysql
from flask import Flask

from model.FoodDao import FoodDao
from model.FoodClassDao import FoodClassDao
from service.FoodService import FoodService
from service.FoodClassService import FoodClassService
from view import create_endpoints


class Services:
    pass

################################
# Create App
################################
def create_app():
    app = Flask(__name__)

    # Persistence Layer
    foodDao = FoodDao()
    foodclassDao = FoodClassDao()

    # Businsess Layer
    services = Services
    services.foodService = FoodService(foodDao)
    services.foodClassService = FoodClassService(foodclassDao)

    ## 엔드포인트들을 생성
    create_endpoints(app, services)
    return app

if __name__ == "__main__":
    app = create_app()
    app.run(host='0.0.0.0', port=8080)