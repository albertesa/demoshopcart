export class Product {
  public id: string;
  public productName: string;
  public productDesc: string;
  public productImg: string;

  constructor(id: string, name: string, desc: string, img: string) {
    this.id = id;
    this.productName = name;
    this.productDesc = desc;
    this.productImg = img;
  }
}
