import { Component, Input, Output, EventEmitter } from '@angular/core';

import { readModel } from './read.model';

@Component({
    selector: 'readontologia',
    templateUrl: 'read.component.html'
})
export class ReadComponent {

    @Input()
    private readontologia: readModel;
}