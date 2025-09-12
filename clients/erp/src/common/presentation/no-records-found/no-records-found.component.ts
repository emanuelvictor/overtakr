import {Component, Input} from '@angular/core';

@Component({
    selector: 'no-records-found',
    templateUrl: 'no-records-found.component.html',
    styleUrls: ['./no-records-found.component.scss'], standalone: false
})
export class NoRecordsFoundComponent {
    @Input() dataSource: any;
}
