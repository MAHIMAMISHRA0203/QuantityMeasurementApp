package com.example.quantity_measurement_app.dto;
public class QuantityDTO {

    private double value;
    private String unit;
    private String measurementType;

    public QuantityDTO(double value, String unit, String measurementType) {
        this.value = value;
        this.unit = unit;
        this.measurementType = measurementType;
    }

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public String getMeasurementType() {
        return measurementType;
    }

    //LENGTH UNITS 

    public enum LengthUnit {

        FEET("FEET","LENGTH"),
        INCHES("INCHES","LENGTH"),
        YARDS("YARDS","LENGTH"),
        CENTIMETERS("CENTIMETERS","LENGTH");

        private final String unitName;
        private final String measurementType;

        LengthUnit(String unitName,String measurementType){
            this.unitName = unitName;
            this.measurementType = measurementType;
        }

        public String getUnitName(){
            return unitName;
        }

        public String getMeasurementType(){
            return measurementType;
        }
    }

    // WEIGHT UNITS 

    public enum WeightUnit {

        GRAM("GRAM","WEIGHT"),
        KILOGRAM("KILOGRAM","WEIGHT"),
        TONNE("TONNE","WEIGHT"),
        POUND("POUND","WEIGHT");

        private final String unitName;
        private final String measurementType;

        WeightUnit(String unitName,String measurementType){
            this.unitName = unitName;
            this.measurementType = measurementType;
        }

        public String getUnitName(){
            return unitName;
        }

        public String getMeasurementType(){
            return measurementType;
        }
    }

    // VOLUME UNITS

    public enum VolumeUnit {

        LITRE("LITRE","VOLUME"),
        MILLILITRE("MILLILITRE","VOLUME");

        private final String unitName;
        private final String measurementType;

        VolumeUnit(String unitName,String measurementType){
            this.unitName = unitName;
            this.measurementType = measurementType;
        }

        public String getUnitName(){
            return unitName;
        }

        public String getMeasurementType(){
            return measurementType;
        }
    }

    //TEMPERATURE UNITS

    public enum TemperatureUnit {

        CELSIUS("CELSIUS","TEMPERATURE"),
        FAHRENHEIT("FAHRENHEIT","TEMPERATURE");

        private final String unitName;
        private final String measurementType;

        TemperatureUnit(String unitName,String measurementType){
            this.unitName = unitName;
            this.measurementType = measurementType;
        }

        public String getUnitName(){
            return unitName;
        }

        public String getMeasurementType(){
            return measurementType;
        }
    }
}