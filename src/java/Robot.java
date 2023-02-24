package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.motorcontrol.Spark;

import java.time.LocalDateTime;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;


public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */

    // Teleop
    // Motores Chasis
    private Spark m_leftMotor = new Spark(0); 
    private Spark m_rightMotor = new Spark(1);
    //Declaracion Controles
    private Joystick driverJoystick = new Joystick(0);
    private Joystick operatorJoystick = new Joystick(1);
    //Motores Brazo
    private CANSparkMax m_neo = new CANSparkMax(3, MotorType.kBrushless);
    private CANSparkMax m_redline = new CANSparkMax(4, MotorType.kBrushed);
    //Declaracion electrovalvulas en PCM
    Solenoid piston1 = new Solenoid(PneumaticsModuleType.CTREPCM, 0);
    Solenoid piston2 = new Solenoid(PneumaticsModuleType.CTREPCM, 1);
    boolean Acci_Pist = false; // variable para el accionamiento de piston

    // Autonomous
    private double startTime;

  @Override
  public void robotInit() {}

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {

    startTime = Timer.getFPGATimestamp();

  }

  @Override
  public void autonomousPeriodic() {

    double time = Timer.getFPGATimestamp();

    if (time - startTime < 3){
    m_leftMotor.set(0.47);
    m_rightMotor.set(-0.6);
    } else {
       m_leftMotor.set(0);
       m_rightMotor.set(0);   
    }
  }

  @Override
  public void teleopInit() {}

  private boolean toggledvalue = false;
private LocalDateTime lastToggleDateTime =java.time.LocalDateTime.now(); 


  @Override
  public void teleopPeriodic() {

    // Configuracion movimiento del robot con correccion de curso

    double turn = driverJoystick.getRawAxis(2) * 0.3;
    turn = turn + turn * 0.1;

    double speed = -driverJoystick.getRawAxis(1) * 0.75;
    double correction = driverJoystick.getRawAxis(1) * 0.12;

    double left = speed + turn;
    double right = turn - speed;

    double lmotor = left;
    double rmotor = right + correction;

    m_leftMotor.set(lmotor);
    m_rightMotor.set(rmotor);

    // Configuracion control brazo

    if (operatorJoystick.getRawButton(7)){
      m_neo.set(-0.1D);
    } else if (operatorJoystick.getRawButton(8)){
      m_neo.set(0.1D);
    } else
    {
      m_neo.set(0D);
    }

    if (operatorJoystick.getRawButton(5)){
      m_redline.set(0.1D);
    } else if (operatorJoystick.getRawButton(6)){
      m_redline.set(-0.1D);
    } else{
      m_redline.set(0D);
    }

    // Configuracion pistones/electrovalvulas
     /*
     * El piston conectado por el puerto 0 de la PCM se prendera
     * cuando se mantenga presionado el boton A del control conectado
     * por el puerto 0 de la Drive Station (El piston estara afuera siempre que se presione ese boton)
     */

     if(operatorJoystick.getRawButton(1))
     {

      long timespandeounce = 1;
      if (java.time.LocalDateTime.now().compareTo(lastToggleDateTime.plusSeconds(timespandeounce)) > 0)
      {
           lastToggleDateTime = java.time.LocalDateTime.now();
           toggledvalue = !toggledvalue;
      }
     }
     piston1.set(toggledvalue);
     



     /*
     * El piston conectado por el puerto 1 de la PCM se prendera
     * cuando se presione el boton B del control
     * por el puerto 0 de la Drive Station, sin la necesidad de dejarle presionado
     */

  //    if (operatorJoystick.getRawButtonReleased(2)) {      //Cuando se presione el boton del control entrara al if

  //    if (Acci_Pist) {                          /*Si detecta que la variable esta en true accionara el piston
  //                                             y pondra la variable en false para que la proxima vez que se 
  //                                             presione el boton se cambie al lugar donde la variable es false y 
  //                                             pondra la variable en true para que asi se cicle */
  //     piston2.set(true);
  //     Acci_Pist = false;
      
  //   } else {

  //     piston2.set(false);
  //     Acci_Pist = true;

  //   }
  // }

  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
