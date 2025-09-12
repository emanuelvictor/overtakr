import {Component, Input} from '@angular/core';

// @ts-ignore
@Component({
  selector: 'button-toggle-advanced-filters',
  templateUrl: 'button-toggle-advanced-filters.component.html',
  styleUrls: ['./button-toggle-advanced-filters.component.scss'],
  standalone: false
})
export class ButtonToggleAdvancedFiltersComponent {
  @Input() advancedFiltersActive: boolean | undefined;
}
