import { Component, Input, Output, EventEmitter } from '@angular/core';

import { linkModel } from './link.model';

@Component({
    selector: 'linkontologia',
    templateUrl: 'link.component.html'
})
export class LinkComponent {

    @Input()
    private linkontologia: linkModel;
}