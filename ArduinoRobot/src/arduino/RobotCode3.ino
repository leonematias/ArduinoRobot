/**
 * Arduino Elegoo robot
 *
 * Matias Leone
 */


//Commands
const char PING = 'a';



//Servo library
#include <Servo.h>
Servo myServo;
const int Echo = A3;  
const int Trig = A2;

//Gyroscope library
#include <Wire.h>
#include <Adafruit_Sensor.h>
#include <Adafruit_BNO055.h>
#include <utility/imumaths.h>
#define BNO055_SAMPLERATE_DELAY_MS (100)
Adafruit_BNO055 bno = Adafruit_BNO055(55);

//LED
const int LED = 13;

//Wheels motors
const int in1 = 9;
const int in2 = 8;
const int in3 = 7;
const int in4 = 6;
const int ENA = 11;
const int ENB = 5;
int currentSpeed = MID_ABS;


//Control variables
int wheelsMotorSpeed = 100;
int led = false;


const char COMMAMD_SEP = '\n';
const char PARAM_SEP = '|';
const int MAX_COMMAND_PARAMS = 3;

struct Command {
    String command;
    int paramsCount;
    String params[COMMAND];
};


//------------------------ Utils ------------------------//

/**
 * Read incoming Serial command with format: Command;Param1;Param2;Param3;\n
 */
Command readInputCommand() {
    //Init empty command
    Command cmd;
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
            return cmd;

        //Parse command
        int sepIndex = str.indexOf(PARAM_SEP);
        if(sepIndex < 0)
            return cmd;
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

    return cmd;
}

void returnOk(Command cmd) {
    String response = cmd.command;
    response += "_OK";
    Serial.println(response);
}


//##############################################################################


/**
 * Initial setup
 */
void setup() {
    //Init LED
    pinMode(LED, OUTPUT);

    //Init Bluetooth
    Serial.begin(9600);

    //Init motors
    pinMode(in1,OUTPUT);
    pinMode(in2,OUTPUT);
    pinMode(in3,OUTPUT);
    pinMode(in4,OUTPUT);
    pinMode(ENA,OUTPUT);
    pinMode(ENB,OUTPUT);

    //Init servo
    myServo.attach(3);// attach servo on pin 3 to servo object
    pinMode(Echo, INPUT);    
    pinMode(Trig, OUTPUT);  

    //Stop wheels
    stopMovement();
}

/**
 * Main loop
 */
void loop() {

    //Read incomming Bluetooth commands
    Command cmd = readInputCommand();
    if(cmd.command == "")
        return;

    
    if(cmd.command == "PING") {
        returnOk(cmd);

    } else if(cmd.command == "SPEED") {
        wheelsMotorSpeed = cmd.params[0].toInt();
        returnOk(cmd);
    }
}
