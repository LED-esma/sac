package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Indexer extends SubsystemBase {

    private SparkMax IndexMotor;

    public Indexer(int IndexMoterID){
        
        IndexMotor = new SparkMax(IndexMoterID, MotorType.kBrushless);

    }

    public enum IndexState {
        INDEX_IN(5.0),
        INDEX_OUT(-5.0),
        INDEX_IDLE(0);

        public final double Speed;
        private IndexState(double speed){
            Speed = speed;

        }
    }

    public void setSpeed(double speed){
        IndexMotor.set(speed);
    }

    public IndexState currentState = IndexState.INDEX_IDLE;

    public void setState(IndexState wantedState){
        currentState = wantedState;
        setSpeed(wantedState.Speed);
    }
    
    public Command INDEX_IN() {
        return Commands.runOnce(() -> setState(IndexState.INDEX_IN), this);

    }
    public Command INDEX_OUT() {
        return Commands.runOnce(() -> setState(IndexState.INDEX_OUT), this);

    } 
    public Command INDEX_IDLE() {
        return Commands.runOnce(() -> setState(IndexState.INDEX_IDLE), this);

    }
    
    public Command INDEXCYCLE(){
        return Commands.sequence(
            INDEX_OUT(),
            Commands.waitSeconds(3),
            INDEX_IN(),
            Commands.waitSeconds(2),
            INDEX_IDLE()
        );
    }
}
