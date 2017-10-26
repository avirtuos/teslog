package org.avirtuos.teslog.api.domain;


public class TelemetryMessage {
    private final long sampleTime;
    private final long speed;
    private final long odometerReading;
    private final float batteryPercent;
    private final float elevation;
    //its unclear to me what the difference between estHeading and heading is
    private final float estHeading;
    private final float heading;
    private final float latitude;
    private final float longitude;
    private final float power;
    private final String shiftState;
    private final float ratedRange;
    private final float estRange;

    private TelemetryMessage(Builder builder) {
        sampleTime = builder.sampleTime;
        speed = builder.speed;
        odometerReading = builder.odometerReading;
        batteryPercent = builder.batteryPercent;
        elevation = builder.elevation;
        estHeading = builder.estHeading;
        heading = builder.heading;
        latitude = builder.latitude;
        longitude = builder.longitude;
        power = builder.power;
        shiftState = builder.shiftState;
        ratedRange = builder.ratedRange;
        estRange = builder.estRange;
    }



    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(TelemetryMessage copy) {
        Builder builder = new Builder();
        builder.sampleTime = copy.getSampleTime();
        builder.speed = copy.getSpeed();
        builder.odometerReading = copy.getOdometerReading();
        builder.batteryPercent = copy.getBatteryPercent();
        builder.elevation = copy.getElevation();
        builder.estHeading = copy.getEstHeading();
        builder.heading = copy.getHeading();
        builder.latitude = copy.getLatitude();
        builder.longitude = copy.getLongitude();
        builder.power = copy.getPower();
        builder.shiftState = copy.getShiftState();
        builder.ratedRange = copy.getRatedRange();
        builder.estRange = copy.getEstRange();
        return builder;
    }


    public long getSampleTime() {
        return sampleTime;
    }

    public long getSpeed() {
        return speed;
    }

    public long getOdometerReading() {
        return odometerReading;
    }

    public float getBatteryPercent() {
        return batteryPercent;
    }

    public float getElevation() {
        return elevation;
    }

    public float getEstHeading() {
        return estHeading;
    }

    public float getHeading() {
        return heading;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getPower() {
        return power;
    }

    public String getShiftState() {
        return shiftState;
    }

    public float getRatedRange() {
        return ratedRange;
    }

    public float getEstRange() {
        return estRange;
    }

    @Override
    public String toString() {
        return "TelemetryMessage{" +
                "sampleTime=" + sampleTime +
                ", speed=" + speed +
                ", odometerReading=" + odometerReading +
                ", batteryPercent=" + batteryPercent +
                ", elevation=" + elevation +
                ", estHeading=" + estHeading +
                ", heading=" + heading +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", power=" + power +
                ", shiftState='" + shiftState + '\'' +
                ", ratedRange=" + ratedRange +
                ", estRange=" + estRange +
                '}';
    }

    public static final class Builder {
        private long sampleTime;
        private long speed;
        private long odometerReading;
        private float batteryPercent;
        private float elevation;
        private float estHeading;
        private float heading;
        private float latitude;
        private float longitude;
        private float power;
        private String shiftState;
        private float ratedRange;
        private float estRange;

        private Builder() {
        }

        public Builder withSampleTime(long val) {
            sampleTime = val;
            return this;
        }

        public Builder withSpeed(long val) {
            speed = val;
            return this;
        }

        public Builder withOdometerReading(long val) {
            odometerReading = val;
            return this;
        }

        public Builder withBatteryPercent(float val) {
            batteryPercent = val;
            return this;
        }

        public Builder withElevation(float val) {
            elevation = val;
            return this;
        }

        public Builder withEstHeading(float val) {
            estHeading = val;
            return this;
        }

        public Builder withHeading(float val) {
            heading = val;
            return this;
        }

        public Builder withLatitude(float val) {
            latitude = val;
            return this;
        }

        public Builder withLongitude(float val) {
            longitude = val;
            return this;
        }

        public Builder withPower(float val) {
            power = val;
            return this;
        }

        public Builder withShiftState(String val) {
            shiftState = val;
            return this;
        }

        public Builder withRatedRange(float val) {
            ratedRange = val;
            return this;
        }

        public Builder withEstRange(float val) {
            estRange = val;
            return this;
        }

        public TelemetryMessage build() {
            return new TelemetryMessage(this);
        }
    }
}
