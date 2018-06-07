import java.util.*;
import java.awt.Color;
public class Car{
	private Grid myGrid;
	private Location loc;
	private ArrayList<Path> myRoute;
	private ArrayList<Boolean> directions;
    private ArrayList<ArrayList<LineSegment>> speedProfile=new ArrayList<ArrayList<LineSegment>>();//speed profile for each 
    private Route theRoute;
	private Path curPath;
	private double speed=200.0;
	private Intersection destination;
    private Intersection start;
	private int inc=0;
	private Intersection mySpot;
	private Path eatenPath;
	private boolean destIsInt=false;
    private boolean startIsInt=false;
    private Path eatenPath2;
    private boolean killMe=false;
    private Path tempPath1;
    private Path tempPath2;
    private Path tempPath3;
    private double trailingDistance;
    public double getTD(){
        return trailingDistance;
    }
    private double startTime;//in seconds with millisecond precision
	Car(Location l){
        this.trailingDistance=0.4064;
        
		this.myRoute=new ArrayList<Path>();
		this.directions=new ArrayList<Boolean>();
		this.myGrid=l.getGrid();
		this.loc=l;
		l.snapToPath();
		this.destination=new Intersection(l);
		myGrid.setup();
		this.curPath=this.loc.snapToPath();
		this.myGrid.addCar(this);
	}
	Car(Location l,Location d){
        this.trailingDistance=0.4064;
        
		this.myRoute=new ArrayList<Path>();
		this.directions=new ArrayList<Boolean>();
		this.myGrid=l.getGrid();
		this.myGrid.addCar(this);
		
		/*Path cp=l.snapToPath();
		mySpot=new Intersection(l,true);
		Path a1=new Path(cp.getEnd(),mySpot,cp.getSpeedLim(),true);
		Path a2=new Path(cp.getStart(),mySpot,cp.getSpeedLim(),true);
		myGrid.removePath(cp);
		cp.getEnd().removePath(cp);
		cp.getStart().removePath(cp);*/
		this.setup(l,d);
		
	}
    public double getSpeed(){
        return this.speed;
    }
	public void setup(Location l,Location d) {
        
		this.loc=l;
		Path pb=d.snapToPath();
        Path pb2=l.snapToPath();
        if (destination!=null)
            destination.specialize();
         if (start!=null)
            start.specialize();
		this.destination=new Intersection(d,true);
		destIsInt=false;
		for (int i=0; i<myGrid.getMyIntersections().size()-1;i++) {
			if(myGrid.getMyIntersections().get(i).tryToEat(destination)) {
				destIsInt=true;
                destination.specialize();
				destination=myGrid.getMyIntersections().get(i);
                destination.specialize();
			}
		}
        myGrid.removeDelayedInt();
        this.start=new Intersection(new Location(l),true);
        startIsInt=false;
        for (int i=0; i<myGrid.getMyIntersections().size()-1;i++) {
			if(myGrid.getMyIntersections().get(i).tryToEat(start)) {
				startIsInt=true;
                //start.specialize();
				start=myGrid.getMyIntersections().get(i);
                //start.specialize();
			}
		}
		myGrid.removeDelayedInt();
		if (!destIsInt) {
		Path b1=new Path(destination,pb.getEnd(),pb.getSpeedLim(),false);
		Path b2=new Path(pb.getStart(),destination,pb.getSpeedLim(),false);
        
		eatenPath=pb;
		pb.getEnd().removePath(pb);
		pb.getStart().removePath(pb);
		}
        if (!startIsInt) {
		Path b1=new Path(start,pb2.getEnd(),pb2.getSpeedLim(),false);
		Path b2=new Path(pb2.getStart(),start,pb2.getSpeedLim(),false);

            
		eatenPath2=pb2;
		pb2.getEnd().removePath(pb2);
		pb2.getStart().removePath(pb2);
        }
        
        //System.out.print(" middle ");
		//System.out.print(myGrid.getMyIntersections().size());
		//myGrid.setup();
		//System.out.print(" end ");
		//System.out.println(myGrid.getMyIntersections().size());
		this.curPath=this.loc.snapToPath();
		theRoute=this.getOptimalPath();
	}
	public void update() {
        
        //System.out.println(myGrid.getTime());
        //System.out.println("should have showed");
        ArrayList<LineSegment> nowIntensity = this.speedProfile.get(inc-1);
        //System.out.println(nowIntensity);
        double temp=ExtraMethods.parseSpeed(myGrid.getTime(),nowIntensity);
        double newLoc=ExtraMethods.parseLoc(myGrid.getTime(),nowIntensity);
        //System.out.println(newLoc);
        //System.out.println(newLoc);
        //System.out.println(newLoc);
        //System.out.println(myGrid.getTime());
        if(!Double.isNaN(temp)){
            this.speed=temp;
            //System.out.println("yay");
        }
        //System.out.println(this.speed);
            
            
		if(this.directions.get(this.inc-1)) {
			
			speed=Math.abs(speed);
		}
		else {speed=-Math.abs(speed);
		}
        
        int t=this.loc.teleport(curPath,newLoc);
        //int t=0;
		if(t>0) {
            System.out.println("REEEEEEEEEEEET");
			if (this.inc==myRoute.size()) {
				if (!destIsInt) {
					
				Path p1=destination.getPaths().get(0);
				Path p2=destination.getPaths().get(1);
				p1.getOther(destination).addPath(eatenPath);
				p2.getOther(destination).addPath(eatenPath);
				p1.die();
				p2.die();
				myGrid.removeIntersection(destination);
				}
                theRoute.clear();
				/*for(int i=myRoute.size()-1;i>=0;i--) {
					myRoute.remove(i);
					
				}
                for(int i=directions.size()-1;i>=0;i--) {
					directions.remove(i);
					
				}
                for(int i=speedProfile.size()-1; i>=0;i--){
                    speedProfile.remove(i);
                }*/
                
				this.inc=0;
				setup(this.loc,new Location( myGrid,(Math.random()*800),(Math.random()*800)));
			}
			else {
				
			curPath=this.loc.snapToPath(myRoute.get(this.inc));
			
		if(this.directions.get(this.inc)) {
			
			speed=Math.abs(speed);
		}
		else {speed=-Math.abs(speed);
		}
                
		inc++;
			}
		}
		this.show();
        
        //StdDraw.show();
		//CAR UPDATE METHOD
		
	}
	public void show() {
		StdDraw.setPenColor(new Color(0,0,255));
		StdDraw.filledRectangle(this.loc.getPos()[0],this.loc.getPos()[1], 10, 10);
		
	}
    
    /*public void setPathOccupation(int num, Occupation occ){
        //this.speedProfile.set(num,occ);
    }*/
    public void addToSP(int index, ArrayList<LineSegment> ls){
        if(index>=speedProfile.size()){
            //System.out.println("add: "+index+", "+ls.size());
            this.speedProfile.add(ls);
        }
        else{
            //System.out.println("set: "+index+", "+ls.size());
        this.speedProfile.set(index,ls);
        }
    }
	public Route getOptimalPath() {
        
        this.startTime=this.myGrid.getTime();//set start time to be 3 seconds from query time -- we can play with this delay as we test our system
     //System.out.println(this.myGrid.getTime());
		for (Intersection i: myGrid.getMyIntersections()) {
			i.setup();
		}
		
        myGrid.intersectionUpdate();
    
    
      
        this.start.nodify(0,this,null,startTime);
        
		this.theRoute=new Route(this);
        this.directions = new ArrayList<Boolean>();
        if(destination.nodeValue()==Double.POSITIVE_INFINITY){
            System.out.println("UNSOLVABLE");
        }
        //ArrayList<Path> path = destination.collectRoute(this.start,directions,this,startTime);
        theRoute=destination.prepareRoute(this.start,this,this.startTime);

        if (!startIsInt) {
				Path p1=start.getPaths().get(0);
				Path p2=start.getPaths().get(1);
                this.curPath=eatenPath2;
                /*if (p1.getOther(start)==destination){
                        Path a;
                        if (start.compareTo(destination)<0){
                        a=new Path(start,destination,p1.getSpeedLim());
                        }
                        else{
                        a=new Path(destination,start, p1.getSpeedLim());
                        }
                        this.curPath=a;
                        tempPath1=a;
                    killMe=true;
                    //System.out.println("yes");
                }*/
            p1.die();
            p2.getOther(start).addPath(eatenPath2);
            p2.die();
            p1.getOther(start).addPath(eatenPath2);
            myGrid.removeIntersection(start);
				
                
        }
        if(this.directions.get(this.inc)) {
			speed=Math.abs(speed);
            int t=this.loc.travel(curPath,speed,true);
		}
        else{
            speed=-Math.abs(speed);
            int t=this.loc.travel(curPath,speed,true);
        }
        inc++;
        //this.destination.nodify(0,this,null);//bug checking
        
		return theRoute;
	}
	public Location getLocation() {
		return this.loc;
	}
	/*public Intersection getNextIntersection() {
		return this.myRoute.get(0).getEnd();
	}
	public Intersection getDestination() {
		int lastIndex=this.myRoute.size()-1;
		return this.myRoute.get(lastIndex).getEnd();
	}*/
	public Grid getGrid() {
		return myGrid;
	}
    

}
