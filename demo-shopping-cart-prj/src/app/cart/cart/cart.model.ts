import { CartItem } from '../cart-item/cart-item.model';

export class Cart {

  items: CartItem[] = [];

  constructor(
    public id: string,
    public userId: string,
    public numberOfItems: number = 0
    ) {}
}
