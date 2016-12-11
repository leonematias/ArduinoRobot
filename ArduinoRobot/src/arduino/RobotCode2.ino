/**
 * Arduino Elegoo robot
 *
 * Matias Leone
 */


//Commands
const char PING = 'a';
const char STEP_FORWARD = 'b';
const char STEP_BACKWARD = 'c';
const char ROTATE_LEFT_90 = 'd';
const char ROTATE_RIGHT_90 = 'e';
const char TEST_DISTANCE = 'f';
const char DISTANCE_SENSOR_LEFT = 'g';
const char DISTANCE_SENSOR_RIGHT = 'h';
const char DISTANCE_SENSOR_CENTER = 'i';
const char LED_ON = 'j';
const char LED_OFF = 'k';
const char TEST_3_DISTANCES = 'l';
const char TWO_STEPS_FORWARD = 'm';
const char THREE_STEPS_FORWARD = 'n';
const char TWO_STEPS_BACKWARD = 'o';
const char THREE_STEPS_BACKWARD = 'p';
const char ROTATE_LEFT_180 = 'q';
const char SET_SLOW_SPEED = 'r';
const char SET_MID_SPEED = 's';
const char SET_FAST_SPEED = 't';
const char STOP = 'u';


//Servo library
#include <Servo.h>
Servo myServo;
const int Echo = A4;  
const int Trig = A5;

//LED
const int LED = 13;

//Wheels motors
const int in1 = 9;
const int in2 = 8;
const int in3 = 7;
const int in4 = 6;
const int ENA = 10;
const int ENB = 5;
const int SLOW_ABS = 80;
const int MID_ABS = 130;
const int FAST_ABS = 200;
int currentSpeed = MID_ABS;
const int FORWARD_TIME = 200;
const int ROTATE_90_TIME = 240;
const int ROTATE_180_TIME = ROTATE_90_TIME * 2;


//------------------------ Movement ------------------------//

void moveForward() { 
    //analogWrite(ENA,currentSpeed);
    //analogWrite(ENB,currentSpeed);
    digitalWrite(ENA,HIGH);
    digitalWrite(ENB,HIGH);
    digitalWrite(in1,LOW);
    digitalWrite(in2,HIGH);
    digitalWrite(in3,LOW);
    digitalWrite(in4,HIGH);
}

void moveBackward() {
    //analogWrite(ENA,currentSpeed);
    //analogWrite(ENB,currentSpeed);
    digitalWrite(ENA,HIGH);
    digitalWrite(ENB,HIGH);
    digitalWrite(in1,HIGH);
    digitalWrite(in2,LOW);
    digitalWrite(in3,HIGH);
    digitalWrite(in4,LOW);
}

void rotateLeft() {
    //analogWrite(ENA,currentSpeed);
    //analogWrite(ENB,currentSpeed);
    digitalWrite(ENA,HIGH);
    digitalWrite(ENB,HIGH);
    digitalWrite(in1,LOW);
    digitalWrite(in2,HIGH);
    digitalWrite(in3,HIGH);
    digitalWrite(in4,LOW);
}

void rotateRight() {
    //analogWrite(ENA,currentSpeed);
    //analogWrite(ENB,currentSpeed);
    digitalWrite(ENA,HIGH);
    digitalWrite(ENB,HIGH);
    digitalWrite(in1,HIGH);
    digitalWrite(in2,LOW);
    digitalWrite(in3,LOW);
    digitalWrite(in4,HIGH);
}

void stopMovement() {
    digitalWrite(ENA,LOW);
    digitalWrite(ENB,LOW);
}

void stepForward(int n) {
    moveForward();
    delay(FORWARD_TIME * n);
    stopMovement(); 
}

void stepBackward(int n) {
    moveBackward();
    delay(FORWARD_TIME * n);
    stopMovement();
}

void rotateLeft90() {
    rotateLeft();
    delay(ROTATE_90_TIME);
    stopMovement();
}

void rotateLeft180() {
    rotateLeft();
    delay(ROTATE_180_TIME);
    stopMovement();
}

void rotateRight90() {
    rotateRight();
    delay(ROTATE_90_TIME);
    stopMovement();
}


//------------------------ Distance ------------------------//


/**
 * Ultrasonic distance measurement
 */
int distanceTest() {
    digitalWrite(Trig, LOW);   
    delayMicroseconds(2);
    digitalWrite(Trig, HIGH);  
    delayMicroseconds(20);
    digitalWrite(Trig, LOW);   
    float fDistance = pulseIn(Echo, HIGH);  
    fDistance = fDistance / 58;       
    return (int)fDistance;
}

int distanceCenter() {
    myServo.write(90);
    delay(500);
    return distanceTest();
}

int distanceLeft() {
    myServo.write(90);
    delay(1000);
    myServo.write(180);
    delay(1000);
    return distanceTest();
}

int distanceRight() {
    myServo.write(5);
    delay(1000);
    return distanceTest();
}


//------------------------ Misc ------------------------//

void returnOk(char command) {
    String response = String(command);
    response += "_OK";
    Serial.println(response);
}

void returnInt(char command, int result) {
    String response = String(command);
    response += '_';
    response += result;
    Serial.println(response);
}

void return3Int(char command, int res1, int res2, int res3) {
    String response = String(command);
    response += '_';
    response += res1;
    response += '_';
    response += res2;
    response += '_';
    response += res3;
    Serial.println(response);
}

void ledOn() {
    digitalWrite(LED, HIGH);
}

void ledOff() {
    digitalWrite(LED, LOW);
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

    //Read incomming Bluetooth command
    if (Serial.available() > 0) {
        char command = Serial.read();

        //Apply command
        switch (command) {
            case PING:
                returnOk(command);
                break;
            case STEP_FORWARD:
                stepForward(1);
                returnOk(command);
                break;
            case STEP_BACKWARD:
                stepBackward(1);
                returnOk(command);
                break;
            case ROTATE_LEFT_90:
                rotateLeft90();
                returnOk(command);
                break;
            case ROTATE_RIGHT_90:
                rotateRight90();
                returnOk(command);
                break;
            case TEST_DISTANCE:
                returnInt(command, distanceTest());
                break;
            case DISTANCE_SENSOR_LEFT:
                returnInt(command, distanceLeft());
                break;
            case DISTANCE_SENSOR_RIGHT:
                returnInt(command, distanceRight());
                break;
            case DISTANCE_SENSOR_CENTER:
                returnInt(command, distanceCenter());
                returnOk(command);
                break;
            case LED_ON:
                ledOn();
                returnOk(command);
                break;
            case LED_OFF:
                ledOff();
                returnOk(command);
                break;
            case TEST_3_DISTANCES:
                return3Int(command, distanceCenter(), distanceRight(), distanceLeft());
                returnOk(command);
                break;
            case TWO_STEPS_FORWARD:
                stepForward(2);
                returnOk(command);
                break;
            case THREE_STEPS_FORWARD:
                stepForward(3);
                returnOk(command);
                break;
            case TWO_STEPS_BACKWARD:
                stepBackward(2);
                returnOk(command);
                break;
            case THREE_STEPS_BACKWARD:
                stepBackward(3);
                returnOk(command);
                break;
            case ROTATE_LEFT_180:
                rotateLeft180();
                returnOk(command);
                break;
            case SET_SLOW_SPEED:
                currentSpeed = SLOW_ABS;
                returnOk(command);
                break;
            case SET_MID_SPEED:
                currentSpeed = MID_ABS;
                returnOk(command);
                break;
            case SET_FAST_SPEED:
                currentSpeed = FAST_ABS;
                returnOk(command);
                break;
            case STOP:
                stopMovement();
                returnOk(command);
                break;
        }
    }
}
