import java.util.*;
public class Route{
    private Car c;
    private ArrayList<RouteModule> myRoute;
    private int inc=0;
    private boolean done=true;
    private double offset;
    private Grid myGrid;
    public Route(Car c,ArrayList<RouteModule> a){
        this.c=c;
        myRoute=new ArrayList<RouteModule>(a);
        myGrid=c.getGrid();
    }
    public Route(Car c){
        this.c=c;
        myRoute=new ArrayList<RouteModule>();
        myGrid=c.getGrid();
    }
    public Route(Route r){
        this.c=r.getCar();
        this.myRoute=r.getRoute();
        myGrid=c.getGrid();
    }
    public boolean advance(){
        if(done){//offset=myRoute.get(inc).getSpeedMap().get(0).getBeginning()-myGrid.getTime(); done=false;
                //System.out.println("work");
                }
        if(myRoute.get(inc).advance(0)){
            //System.out.println(myRoute.get(inc));
            inc++;
            done=true;
            //myRoute.get(inc).push(offset);
            //System.out.println(myRoute.get(inc));
            
           // System.out.println("koom"+inc);
            
        }
        if (inc==myRoute.size()){
            inc=0;
            this.clear();
            //System.out.println("route over");
        
            return true;
        }
        return false;
    }
    public Car getCar(){
        return c;
    }
    
    public ArrayList<RouteModule> getRoute(){
        return myRoute;
    }
    public ArrayList<Path> getPaths(){
        ArrayList<Path> p = new ArrayList<Path>();
        for(int i=0;i<myRoute.size();i++){
            p.add(myRoute.get(i).getPath());
        }
        return p;
    }
    public ArrayList<Boolean> getDirections(){
        ArrayList<Boolean> p = new ArrayList<Boolean>();
        for(int i=0;i<myRoute.size();i++){
            p.add(myRoute.get(i).getDirection());
        }
        return p;
    }
    public void addModule(RouteModule r){
        myRoute.add(r);
    }
    public void clear(){
        for(int i=myRoute.size()-1; i>=0;i--){
            myRoute.remove(i);
        }
    }
    public void print(double startTime){
        System.out.println("\n\n\n\n");
        System.out.println("x="+startTime);
        for(int i=0;i<myRoute.size();i++){
            myRoute.get(i).print();
            Occupation o = myRoute.get(i).getPath().getPO();
            if(o==null){
                System.out.println("null");
            }
            else{
            myRoute.get(i).getPath().getPO().printEnterTime();}
        }
        System.out.println("\n\n\n\n");
    }
}