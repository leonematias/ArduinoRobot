/**
 * Arduino Elegoo robot
 *
 * Matias Leone
 */


//Gyroscope library
#include <Wire.h>
#include <Adafruit_Sensor.h>
#include <Adafruit_BNO055.h>
#include <utility/imumaths.h>
#define BNO055_SAMPLERATE_DELAY_MS (100)
Adafruit_BNO055 gyro = Adafruit_BNO055(55); 


/**
 * Pins layout
 */
const int PIN_WHEELS_IN1 = 9;
const int PIN_WHEELS_IN2 = 8;
const int PIN_WHEELS_IN3 = 7;
const int PIN_WHEELS_IN4 = 6;
const int PIN_WHEELS_ENA = 11;
const int PIN_WHEELS_ENB = 5;
const int PIN_LED = 13;
const int PIN_DIST_ECHO = A3;
const int PIN_DIST_TRIG = A2;
//BNO055 using analog A4 and A5



//Control variables
int wheelsMotorSpeed = 0;
int led = 0;

//Command
#define COMMAMD_SEP = '\n';
#define PARAM_SEP = '|';
#define MAX_COMMAND_PARAMS = 3;
struct Command {
    String command;
    int paramsCount;
    String params[COMMAND];
};
Command cmd;


//------------------------ Utils ------------------------//

/**
 * Read incoming Serial command with format: Command;Param1;Param2;Param3;\n
 */
void readInputCommand() {
    //Init empty command
    cmd.command = "";
    cmd.paramsCount = 0;
    for(int i = 0; i < MAX_COMMAND_PARAMS; i++) {
        cmd.params[i] = "";
    }
    
    //Read incoming Serial commands
    if (Serial.available() > 0) {

        //Read input string
        String inputString = Serial.readStringUntil(COMMAMD_SEP);
        int len = inputString.length();
        if(len == 0)
            return;

        //Parse command
        int sepIndex = str.indexOf(PARAM_SEP);
        if(sepIndex < 0)
            return;
        cmd.command = str.substring(0, sepIndex);

        //Read params
        int n = sepIndex + 1;
        int paramIndex = 0;
        while(n < len && paramIndex < MAX_COMMAND_PARAMS) {
            sepIndex = str.indexOf(PARAM_SEP, n);
            if(sepIndex < 0)
                break;
            cmd.params[paramIndex++] = str.substring(n, sepIndex);
            n = sepIndex + 1;
        }
    }
}

void returnOk() {
    String response = cmd.command;
    response += "_OK";
    Serial.println(response);
}

void returnInt(int result) {
    String response = cmd.command;
    response += '_';
    response += result;
    Serial.println(response);
}

void return3Double(double res1, double res2, double res3) {
    String response = cmd.command;
    response += '_';
    response += res1;
    response += '_';
    response += res2;
    response += '_';
    response += res3;
    Serial.println(response);
}

/**
 * Ultrasonic distance measurement
 */
int distanceTest() {
    digitalWrite(PIN_DIST_TRIG, LOW);   
    delayMicroseconds(2);
    digitalWrite(PIN_DIST_TRIG, HIGH);  
    delayMicroseconds(20);
    digitalWrite(PIN_DIST_TRIG, LOW);   
    float fDistance = pulseIn(PIN_DIST_ECHO, HIGH);  
    fDistance = fDistance / 58;       
    return (int)fDistance;
}


//##############################################################################


/**
 * Initial setup
 */
void setup() {
    //Init LED
    pinMode(PIN_LED, OUTPUT);

    //Init Bluetooth
    Serial.begin(9600);

    //Init motors
    pinMode(PIN_WHEELS_IN1, OUTPUT);
    pinMode(PIN_WHEELS_IN2, OUTPUT);
    pinMode(PIN_WHEELS_IN3, OUTPUT);
    pinMode(PIN_WHEELS_IN4, OUTPUT);
    pinMode(PIN_WHEELS_ENA, OUTPUT);
    pinMode(PIN_WHEELS_ENB, OUTPUT);

    //Init Distance sensor
    pinMode(PIN_DIST_ECHO, INPUT);    
    pinMode(PIN_DIST_TRIG, OUTPUT);  

    //Stop wheels
    digitalWrite(PIN_WHEELS_ENA, LOW);
    digitalWrite(PIN_WHEELS_ENB, LOW);

    //Initialise gyroscope
    if(!gyro.begin()) {
      Serial.print("Ooops, no BNO055 detected ... Check your wiring or I2C ADDR!");
      while(1);
    }
    delay(1000);
    gyro.setExtCrystalUse(true);


}

/**
 * Main loop
 */
void loop() {

    //Read incomming Bluetooth commands and populate global var cmd
    readInputCommand();
    if(cmd.command == "")
        return;

    //Ping command
    if(cmd.command == "PING") {
        returnOk();

    //Enable/disable led
    } else if(cmd.command == "LED") {
        led = cmd.params[0].toInt();
        digitalWrite(PIN_LED, led == 1 ? HIGH : LOW);
        returnOk();

    //Set wheels speed
    } else if(cmd.command == "SPD") {
        wheelsMotorSpeed = cmd.params[0].toInt();
        analogWrite(PIN_WHEELS_ENA, wheelsMotorSpeed);
        analogWrite(PIN_WHEELS_ENB, wheelsMotorSpeed);
        returnOk();

    //Set wheels in forward mode
    } else if(cmd.command == "FORW") {
        digitalWrite(PIN_WHEELS_IN1, LOW);
        digitalWrite(PIN_WHEELS_IN2, HIGH);
        digitalWrite(PIN_WHEELS_IN3, LOW);
        digitalWrite(PIN_WHEELS_IN4, HIGH);
        returnOk();

    //Set wheels in backward mode
    } else if(cmd.command == "BACK") {
        digitalWrite(PIN_WHEELS_IN1, HIGH);
        digitalWrite(PIN_WHEELS_IN2, LOW);
        digitalWrite(PIN_WHEELS_IN3, HIGH);
        digitalWrite(PIN_WHEELS_IN4, LOW);
        returnOk();

    //Stop wheels movement
    } else if(cmd.command == "STOP") {
        wheelsMotorSpeed = 0;
        analogWrite(PIN_WHEELS_ENA, wheelsMotorSpeed);
        analogWrite(PIN_WHEELS_ENB, wheelsMotorSpeed);
        returnOk();

    //Set wheels in rotate left mode
    } else if(cmd.command == "RLEFT") {
        digitalWrite(PIN_WHEELS_IN1, LOW);
        digitalWrite(PIN_WHEELS_IN2, HIGH);
        digitalWrite(PIN_WHEELS_IN3, HIGH);
        digitalWrite(PIN_WHEELS_IN4, LOW);
        returnOk();

    //Set wheels in rotate right mode
    } else if(cmd.command == "RRIGHT") {
        digitalWrite(PIN_WHEELS_IN1, HIGH);
        digitalWrite(PIN_WHEELS_IN2, LOW);
        digitalWrite(PIN_WHEELS_IN3, LOW);
        digitalWrite(PIN_WHEELS_IN4, HIGH);
        returnOk();

    //Distance test
    } else if(cmd.command == "DIST") {
        int distance = distanceTest();
        returnInt(distance);

    //Get gyroscope info
    } else if(cmd.command == "GYRO") {
        sensors_event_t event;
        gyro.getEvent(&event);
        return3Double(event.orientation.x, orientation.y, orientation.z);
        //delay(BNO055_SAMPLERATE_DELAY_MS);
    }
}
