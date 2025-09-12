import { Component, Output, EventEmitter } from '@angular/core';

@Component({
    selector: 'button-clear-filters',
    templateUrl: 'button-clear-filters.component.html', standalone: false
})
export class ButtonClearFiltersComponent {

    @Output() clear = new EventEmitter();

    clearFilters = () => this.clear.emit();
}
