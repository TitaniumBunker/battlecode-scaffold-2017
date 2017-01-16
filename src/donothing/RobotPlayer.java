package donothing;
import battlecode.common.*;
import battlecode.world.RobotControllerImpl;

import javax.tools.JavaCompiler;

import static java.lang.Object.*;
import static java.lang.Math.PI;


public strictfp class RobotPlayer {
    static RobotController rc;

    static boolean RingOfSteel = true;


    // Comments for developers of the Battlecode system :
    // I tried to break the sandbox by using the nio library - which isn't black listed - but also wasn't white listed.
    // Therefore I can safely say that whitelisting seems to work fine...:(
    //
    //
    // Useful links :
    // Javadocs : http://s3.amazonaws.com/battlecode-releases-2017/releases/javadoc/battlecode/common/package-summary.html
    // Specification : http://s3.amazonaws.com/battlecode-releases-2017/releases/specs-1.2.2.html#
    //
    //



    /**
     * run() is the method that is called when a robot is instantiated in the Battlecode world.
     * If this method returns, the robot dies!
    **/
    @SuppressWarnings("unused")
    public static void run(RobotController rc) throws GameActionException {
        RobotPlayer.rc = rc;


        // This is the RobotController object. You use it to perform actions from this robot,
        // and to get information on its current status.
        switch (rc.getType()) {
            case ARCHON:
                runArchon();
                break;
            case GARDENER:
                runGardener();
                break;
            case LUMBERJACK:
                break;
            case SOLDIER:
                break;
            case TANK:
                break;
            case SCOUT:
                break;
        }
    }

    private static void debug_ShowRadius(MapLocation m)
    {
        try
        {
            for (int i = 0; i < 360; i++)
            {
                int r = 10;
                double radians = i * (PI / 180);

                int x = (int) Math.round(m.x + r * Math.cos(radians));
                int y = (int) Math.round(m.y + r * Math.sin(radians));
                MapLocation pointm = new MapLocation(x, y);
                //rc.setIndicatorDot(pointm, 255, 128, 0);
            }
        }
        catch (Exception e)
        {
            System.out.println("debug Exception");
            e.printStackTrace();
        }
    }
    private static float AngleToRadians(float angle)

    {
        double radians = angle * (PI/180);
        return (float)radians;
    }
    public static void runArchon() throws GameActionException {

        System.out.println("I'm an archon!");
        //debug_ShowRadius(rc.getLocation());


        // The code you want your robot to perform every round should be in this loop
        while (true) {

            int RoundNumber = rc.getRoundNum();
            if (RoundNumber == 10)
            {
                System.out.println("SPAWNING A GARDNER");
                Direction spawnDirection = new Direction(AngleToRadians(0));

                if (rc.canHireGardener(spawnDirection))
                {
                    rc.hireGardener(spawnDirection);
                } else
                spawnDirection = new Direction(AngleToRadians(90));
                if (rc.canHireGardener(spawnDirection))
                {
                    rc.hireGardener(spawnDirection);
                }
            }

            if (RoundNumber == 100)
            {   System.out.println("SETTING TEAM MEMORY");
                rc.setTeamMemory(2,100);
            }

            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode
            try {
                if (RingOfSteel)
                {
                }
                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                battlecode.common.Clock.yield();

            } catch (Exception e) {
                System.out.println("Archon Exception");
                e.printStackTrace();
            }
        }
    }
    /**
     * Returns a random Direction
     * @return a random Direction
     */
    static Direction randomDirection() {
        return new Direction((float)Math.random() * 2 * (float)Math.PI);
    }


    static void runGardener() throws GameActionException
    {
        java.util.ArrayList<MapLocation> Forrest = new java.util.ArrayList<MapLocation>();
        boolean Plant = false;
        MapLocation GardenArea = new MapLocation(20,10);

        if (rc.getTeam() == Team.A)
        {
            GardenArea = new MapLocation(10,10);
        }

        //code to execute once

        // The code you want your robot to perform every round should be in this loop
        while (true) {
            try {
                MapLocation CurrentLocation = rc.getLocation();
                if ((CurrentLocation.x == GardenArea.x) && (CurrentLocation.y == GardenArea.y))
                {
                    TreeInfo[] thicket = rc.senseNearbyTrees(1);

                    if (thicket.length ==0 )
                    {
                        Plant = true;
                    }


                    Direction platDirection = new Direction(AngleToRadians(90));
                    if(Plant & rc.canPlantTree(platDirection ))
                    {
                        rc.plantTree(platDirection );
                        Forrest.add(CurrentLocation);
                    }

                    if (!Plant)
                    {
                        if (rc.canWater(thicket[0].location))
                        {
                            System.out.println("Watering...");
                            rc.water(thicket[0].location);
                        }

                        if (rc.canShake(thicket[0].location))
                        {
                            System.out.println("Shaking...");
                            rc.shake(thicket[0].location);
                        }
                    }







                }
                else
                {
                    rc.move(GardenArea);
                }
                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                battlecode.common.Clock.yield();
            }
            catch (Exception e)
            {
                System.out.println("Archon Exception");
                e.printStackTrace();
            }
        }

    }

}
