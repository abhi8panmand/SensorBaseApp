// ISensorAidlInterface.aidl
package com.gravity.sensorapplication;

// Declare any non-default types here with import statements

interface ISensorAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    void getSeonsorData(float x, float y, float z);

}