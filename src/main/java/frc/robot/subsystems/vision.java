package frc.robot.subsystems;

import java.util.Optional;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class vision extends SubsystemBase {

    // PhotonVision camera
    private final PhotonCamera cam = new PhotonCamera("limelight-photon");

    // Field AprilTag layout
    private static final AprilTagFieldLayout kTagLayout = AprilTagFieldLayout.loadField(AprilTagFields.kDefaultField);

    // Robot → Camera transform (MEASURE THIS ON YOUR ROBOT)
    private static final Transform3d kRobotToCam = new Transform3d(
            new Translation3d(0.5, 0.0, 0.5),
            new Rotation3d(0, 0, 0));

    // Pose estimator
    private final PhotonPoseEstimator photonEstimator = new PhotonPoseEstimator(
            kTagLayout,
            PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR,
            kRobotToCam);

    // Hub position on field (meters)
    private static final Translation2d HUB_POSITION = new Translation2d(4.6, 4.0);

    // Stored robot pose from vision
    private Pose2d latestVisionPose = new Pose2d();

    // Distance from robot to hub
    private double distance = 0.0;

    public vision() {
    }

    /**
     * Returns distance from robot to hub in meters
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Returns latest estimated robot pose from vision
     */
    public Pose2d getVisionPose() {
        return latestVisionPose;
    }

    @Override
    public void periodic() {

        Optional<EstimatedRobotPose> visionEst = Optional.empty();

        for (var result : cam.getAllUnreadResults()) {

            visionEst = photonEstimator.update(result);

            if (visionEst.isPresent()) {

                EstimatedRobotPose estPose = visionEst.get();

                latestVisionPose = estPose.estimatedPose.toPose2d();

                // Calculate distance robot -> hub
                distance = latestVisionPose
                        .getTranslation()
                        .getDistance(HUB_POSITION);
            }
        }
    }
}