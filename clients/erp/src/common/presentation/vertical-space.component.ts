import {Component, Input} from '@angular/core';

@Component({
    selector: 'vertical-space',
    template: `
        <div layout="row" [ngStyle]="{'margin': verticalMargin ? verticalMargin + 'px 0' : '10px 0'}"></div>`, standalone: false
})
export class VerticalSpaceComponent {
    @Input() verticalMargin: number | undefined;
}
