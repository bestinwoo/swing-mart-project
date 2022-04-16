package db;
/** 상품 Value Object */
public class GoodsVO {
	int id;
	String goodsName;
	int price;
	int count;
	String category;
	
	public GoodsVO(int id, String goodsName, int price, int count, String category) {
		this.id = id;
		this.goodsName = goodsName;
		this.price = price;
		this.count = count;
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
