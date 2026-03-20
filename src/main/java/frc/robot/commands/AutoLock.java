package frc.robot.commands;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.vision;
import java.util.function.DoubleSupplier;

public class AutoLock extends Command {

    private CommandSwerveDrivetrain drive;
    private SwerveRequest.RobotCentricFacingAngle aim = new SwerveRequest.RobotCentricFacingAngle();

    private static final Translation2d HUB_POSITION = new Translation2d(4.6, 4);

    private DoubleSupplier xSupplier, ySupplier;

    public AutoLock(CommandSwerveDrivetrain drive,  DoubleSupplier xSupplier, DoubleSupplier ySupplier) {
        this.drive = drive;
        this.xSupplier = xSupplier;
        this.ySupplier = ySupplier;
        addRequirements(drive);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        Translation2d robotPosition = drive.getState().Pose.getTranslation();

        Translation2d toHub = HUB_POSITION.minus(robotPosition);
        Rotation2d angleToHub = new Rotation2d(toHub.getX(), toHub.getY());

        drive.setControl(
            aim.withTargetDirection(angleToHub)
               .withVelocityX(xSupplier.getAsDouble())
               .withVelocityY(ySupplier.getAsDouble())
               .withCenterOfRotation(new Translation2d(0, 0))
        );
    }

    @Override
    public void end(boolean interrupted) {
        drive.setControl(new SwerveRequest.Idle());
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}