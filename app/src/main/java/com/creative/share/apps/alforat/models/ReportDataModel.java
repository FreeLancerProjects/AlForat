package com.creative.share.apps.alforat.models;

import java.io.Serializable;
import java.util.List;

public class ReportDataModel implements Serializable {

    private List<ReportModel>data;

    public List<ReportModel> getData() {
        return data;
    }

    public class ReportModel implements Serializable
    {
        private String id_voucher;
        private String vouchers_date;
        private String statement;
        private String madeen_account;
        private String dayen_account;
        private String value;
        private String user_name;
        private String madeen;
        private String dayen;
        private String op_date;
        private String op_num;
        private String op_statement;
        private String op_type;
        private String op_other;
        private String op_dayeen;
        private double op_madeen;
        private double balance;


        public String getId_voucher() {
            return id_voucher;
        }

        public String getVouchers_date() {
            return vouchers_date;
        }

        public String getStatement() {
            return statement;
        }

        public String getMadeen_account() {
            return madeen_account;
        }

        public String getDayen_account() {
            return dayen_account;
        }

        public String getValue() {
            return value;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getMadeen() {
            return madeen;
        }

        public String getDayen() {
            return dayen;
        }

        public String getOp_date() {
            return op_date;
        }

        public String getOp_num() {
            return op_num;
        }

        public String getOp_statement() {
            return op_statement;
        }

        public String getOp_type() {
            return op_type;
        }

        public String getOp_other() {
            return op_other;
        }

        public String getOp_dayeen() {
            return op_dayeen;
        }

        public double getOp_madeen() {
            return op_madeen;
        }


        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }
    }

}
