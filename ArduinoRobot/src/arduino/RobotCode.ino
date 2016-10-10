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
int ENA=10;
int ENB=5;
int ABS=135;

void moveForward() { 
    digitalWrite(ENA,HIGH);
    digitalWrite(ENB,HIGH);
    digitalWrite(in1,LOW);
    digitalWrite(in2,HIGH);
    digitalWrite(in3,LOW);
    digitalWrite(in4,HIGH);
}

void moveBackward() {
    digitalWrite(ENA,HIGH);
    digitalWrite(ENB,HIGH);
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

void rotateServo(int degrees) {
    servoPos = servoPos - degrees;
    if(servoPos < 0) {
        servoPos = 0;
    } else if(servoPos > 180) {
        servoPos = 180;
    }

    myServo.write(servoPos);
    delay(1000);
}

void rotateServoLeft() {
    rotateServo(0);
}

void rotateServoLeft() {
    rotateServo(0);
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
    }

}

