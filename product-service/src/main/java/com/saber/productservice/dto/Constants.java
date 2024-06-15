package com.saber.productservice.dto;

public class Constants {


    public enum StatusCode {
        ACTIVE((byte)1,"active") , InACTIVE((byte)2,"inActive");
        private final   Byte statusCode;
        private final String statusTitle;

        StatusCode(Byte statusCode, String statusTitle) {
            this.statusCode = statusCode;
            this.statusTitle = statusTitle;
        }

        public Byte getStatusCode() {
            return statusCode;
        }

        public String getStatusTitle() {
            return statusTitle;
        }
    }
}
