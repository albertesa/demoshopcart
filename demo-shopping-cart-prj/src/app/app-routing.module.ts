import { ProductEditComponent } from './products/product-edit/product-edit.component';
import { ProductsCartComponent } from './products-cart/products-cart/products-cart.component';
import { SignupComponent } from './auth/signup/signup.component';
import { LoginComponent } from './auth/login/login.component';
import { WelcomeComponent } from './welcome/welcome/welcome.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ProductViewComponent } from './products/product-view/product-view.component';


const routes: Routes = [
  {path: '', component: WelcomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'signup', component: SignupComponent},
  {path: 'prodscart', component: ProductsCartComponent, runGuardsAndResolvers: 'always'},
  {path: 'viewprod/:id', component: ProductViewComponent},
  {path: 'editprod/:id', component: ProductEditComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {onSameUrlNavigation: 'reload'})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
