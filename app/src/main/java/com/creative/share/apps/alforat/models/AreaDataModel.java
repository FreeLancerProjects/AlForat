package com.creative.share.apps.alforat.models;

import java.io.Serializable;
import java.util.List;

public class AreaDataModel implements Serializable {

    private List<AreaModel> areas;
    private List<CenterCostModel> cost_center;



    public List<AreaModel> getAreas() {
        return areas;
    }

    public List<CenterCostModel> getCost_center() {
        return cost_center;
    }

    public static class AreaModel implements Serializable
    {
        private String id_area;
        private String title;

        public AreaModel(String title) {
            this.title = title;
        }

        public String getId_area() {
            return id_area;
        }

        public String getTitle() {
            return title;
        }
    }

    public static class CenterCostModel implements Serializable
    {
        private String cost_center_id;
        private String title;

        public CenterCostModel(String title) {
            this.title = title;
        }

        public String getCost_center_id() {
            return cost_center_id;
        }

        public String getTitle() {
            return title;
        }
    }
}
