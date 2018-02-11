
public class Category {
	private String name;
	private boolean isHidden;
	
	public Category(String name){
		this.name = name;
		this.isHidden = false;
	}
	
	public boolean getHidden(){
		return isHidden;
	}
	
	public void setHidden(boolean isHidden){
		this.isHidden = isHidden;
	}
	
	public String toString(){
		return name;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof Category) {
			Category c = (Category) obj;
			return name.equalsIgnoreCase(c.name) && isHidden == c.isHidden;
		} else {
			return false;
		}
	}
	@Override
	public int hashCode(){
		return (name.hashCode() * 17);		
	}
}
