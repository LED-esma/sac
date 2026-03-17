package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class ButtonBoardControls {

//button board declaration
    private GenericHID buttons; 

    public ButtonBoardControls(int port){
        buttons = new GenericHID(port);
    }

//Use driver station to see which number/channel each button is seen as
//Once found apply to code

//Top left Button
public Trigger Button7(){
    return new Trigger(() -> buttons.getRawButton(7));


}

//Top Right button
public Trigger Button8(){
    return new Trigger (() ->buttons.getRawButton(8));


}

//Middle button
public Trigger Button11(){
    return new Trigger(()-> buttons.getRawButton(11));


}

//Lower right button
public Trigger Button9(){
    return new Trigger (()-> buttons.getRawButton(9));

}

//lower left button
public Trigger Button12(){
    return new Trigger  (()->buttons.getRawButton(12));

}

//single bottom butto
public Trigger Button10(){
    return new Trigger(()-> buttons.getRawButton(10));

}

}
