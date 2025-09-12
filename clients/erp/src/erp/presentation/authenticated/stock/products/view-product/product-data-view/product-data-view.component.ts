import {Component, Input} from '@angular/core';
import {AuthenticatedViewComponent} from '../../../../authenticated-view.component';
import {Product} from '../../../../../../domain/model/product';

@Component({
  selector: 'access-product-data-view',
  templateUrl: 'product-data-view.component.html', standalone: false
})
export class ProductDataViewComponent {

  @Input()
  product: Product | undefined;

  constructor(homeView: AuthenticatedViewComponent) {
    homeView.toolbar.subhead = 'Produto / Detalhes';
  }
}
