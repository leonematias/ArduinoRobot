/**
 * Arduino Elegoo robot
 *
 * Matias Leone
 */

//Servo library
#include <Servo.h>
Servo myServo;
int Echo = A4;  
int Trig = A5;
int servoPos = 0;

//LED
int LED=13;
volatile int ledState = LOW;

//Wheels motors
int in1=9;
int in2=8;
int in3=7;
int in4=6;
int ENA=11;
int ENB=5;
int ABS=130;

void moveForward() { 
    analogWrite(ENA,ABS);
    analogWrite(ENB,ABS);
    digitalWrite(in1,LOW);
    digitalWrite(in2,HIGH);
    digitalWrite(in3,LOW);
    digitalWrite(in4,HIGH);
}

void moveBackward() {
    analogWrite(ENA,ABS);
    analogWrite(ENB,ABS);
    digitalWrite(in1,HIGH);
    digitalWrite(in2,LOW);
    digitalWrite(in3,HIGH);
    digitalWrite(in4,LOW);
}

void moveLeft() {
    analogWrite(ENA,ABS);
    analogWrite(ENB,ABS);
    digitalWrite(in1,LOW);
    digitalWrite(in2,HIGH);
    digitalWrite(in3,HIGH);
    digitalWrite(in4,LOW);
}

void moveRight() {
    analogWrite(ENA,ABS);
    analogWrite(ENB,ABS);
    digitalWrite(in1,HIGH);
    digitalWrite(in2,LOW);
    digitalWrite(in3,LOW);
    digitalWrite(in4,HIGH);
}

void stopMovement() {
    digitalWrite(ENA,LOW);
    digitalWrite(ENB,LOW);
}

void changeLedState() {
    ledState = !ledState;
    digitalWrite(LED, ledState);  
}

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
  fDistance= fDistance/58;       
  return (int)fDistance;
} 

void setServoInPosition(int pos) {
    if(pos < 0) {
        pos = 0;
    } else if(pos > 180) {
        pos = 180;
    }
    int diff = abs(servoPos - pos);
    if(diff > 0) {
        myServo.write(servoPos);
        delay(15 * diff);
    }
}

void rotateServo(int degrees) {
    setServoInPosition(servoPos + degrees);
}

void pointServoToLeft() {
    setServoInPosition(0);
}

void pointServoToRight() {
    setServoInPosition(180);
}

void pointServoToFront() {
    setServoInPosition(90);
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

    //Read incomming Bluetooth message
    char msg = Serial.read();

    //Decode instruction
    if(msg == 'f') {
        moveForward();
    } else if(msg == 'b') {
        moveBackward();
    } else if(msg == 'l') {
        moveLeft();
    } else if(msg == 'r') {
        moveRight();
    } else if(msg == 's') {
        stopMovement();
    } else if(msg == 'A') {
        changeLedState();
    } else if(msg == 'x') {
        Serial.println("Hello World");
    } else if(msg == 'y') {
        pointServoToLeft();
    } else if(msg == 'u') {
        pointServoToRight();
    } else if(msg == 'i') {
        pointServoToFront();
    } else if(msg == 'o') {
        rotateServo(-5);
    } else if(msg == 'p') {
        rotateServo(5);
    } else if(msg == 'h') {
        int dist = distanceTest();
        Serial.println(dist);
    }

}

