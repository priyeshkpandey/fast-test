package com.util.constant;

import static com.util.constant.Endpoint.Entity.USER;

public final class Endpoint {
    private Endpoint() {}
    public static final String ROOT = "/app";
    public static final class PathVariable {
        private PathVariable() {}
        public static final String USER_ID = "userId";
        public static final String PRODUCT_ID = "productId";
        public static final String PAYMENT_GATEWAY = "paymentGateway";
        public static final String ORDER_ID = "orderId";
        public static final String USER_ID_PATH = "{" + USER_ID + "}";
        public static final String PRODUCT_ID_PATH = "{" + PRODUCT_ID + "}";
        public static final String PAYMENT_GATEWAY_PATH = "{" + PAYMENT_GATEWAY + "}";
        public static final String ORDER_ID_PATH = "{" + ORDER_ID + "}";
    }

    public static final class QueryParam {
        private QueryParam() {}
        public static final String QUERY = "q";
    }

    public static final class Entity {
        public static final String PRODUCT = "/product";
        public static final String USER = "/user";
    }
    public static final class UserEndpoint {
        private UserEndpoint(){}
        public static final String USER_ROOT = ROOT + "/user";

        public static final String USER_SIGNUP = "/signup";
        public static final String USER_LOGIN = "/login";
        public static final String USER_LOGOUT = "/logout" + "/" + PathVariable.USER_ID_PATH;
    }
    public static final class InventoryEndpoint {
        private InventoryEndpoint() {}
        public static final String INVENTORY_ROOT = ROOT + "/inv";

        public static final String USER_PRODUCTS = USER + "/" + PathVariable.USER_ID_PATH + Entity.PRODUCT;
        public static final String USER_PRODUCT = USER_PRODUCTS + "/" + PathVariable.PRODUCT_ID_PATH;
    }

    public static final class PaymentGatewayEndpoint {
        private PaymentGatewayEndpoint() {}
        public static final String PAYMENT_GATEWAY_ROOT = ROOT + "/payg";
        public static final String RECEIVE = "/receive";
    }

    public static final class PaymentEndpoint {
        private PaymentEndpoint() {}
        public static final String PAYMENT_ROOT = ROOT + "/pay";
        public static final String SEND = "/send" + "/" + PathVariable.PAYMENT_GATEWAY_PATH;
        public static final String SUCCESS = "/success";
        public static final String FAILURE = "/failure";
    }

    public static final class ShoppingEndpoint {
        private ShoppingEndpoint() {}
        public static final String SHOPPING_ROOT = ROOT + "/shop";
        public static final String SEARCH = "/search";
        public static final String USER_CART = USER + "/" + PathVariable.USER_ID_PATH + "/cart";
        public static final String USER_CHECKOUT = USER + "/" + PathVariable.USER_ID_PATH + "/checkout";
        public static final String USER_PAYMENT = USER + "/" + PathVariable.USER_ID_PATH + "/pay";
        public static final String USER_ORDER = USER + "/" + PathVariable.USER_ID_PATH + "/order" + "/" + PathVariable.ORDER_ID_PATH;
    }
}
