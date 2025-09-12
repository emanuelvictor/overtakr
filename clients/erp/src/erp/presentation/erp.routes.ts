import {RouterModule, Routes} from '@angular/router';
import {AuthenticatedViewComponent} from './authenticated/authenticated-view.component';
import {AuthenticationService} from '../../common/application/services/authentication/authentication.service';
import {NgModule} from '@angular/core';
import {InventoryViewComponent} from './authenticated/stock/inventory-view.component';
import {ProductsViewComponent} from './authenticated/stock/products/products-view.component';
import {ConsultProductsComponent} from './authenticated/stock/products/consult-products/consult-products.component';
import {InsertProductComponent} from './authenticated/stock/products/insert-product/insert-product.component';
import {UpdateProductComponent} from './authenticated/stock/products/update-product/update-product.component';
import {ViewProductComponent} from './authenticated/stock/products/view-product/view-product.component';
import {ConfigurationViewComponent} from './authenticated/configuration/configuration-view.component';
import {StocksViewComponent} from './authenticated/stock/stocks/stocks-view.component';

const routes: Routes = [
  {
    path: '', component: AuthenticatedViewComponent, canActivate: [AuthenticationService],
    children: [
      {path: '', redirectTo: 'configurations', pathMatch: 'full'},
      {
        path: 'stocks',
        component: InventoryViewComponent,
        children: [
          {
            path: '', redirectTo: 'products', pathMatch: 'full',
          },
          {
            path: 'products', component: ProductsViewComponent,
            children: [
              {path: 'get', redirectTo: '', pathMatch: 'full'},
              {path: '', component: ConsultProductsComponent},
              {path: 'insert', component: InsertProductComponent},
              {path: 'edit/:id', component: UpdateProductComponent},
              {path: ':id/edit', component: UpdateProductComponent},
              {path: ':id', component: ViewProductComponent}
            ]
          },
          {
            path: 'stocks', component: StocksViewComponent,
          }
        ]
      },
      {
        path: 'configurations',
        component: ConfigurationViewComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule],
  providers: []
})
export class ErpRoutingModule {
}

