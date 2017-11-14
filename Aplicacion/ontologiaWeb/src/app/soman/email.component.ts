import { Component, Input, Output, EventEmitter } from '@angular/core';

import { emailModel } from './email.model';
import {Observable} from 'rxjs/Rx';

@Component({
    selector: 'emailontologia',
    templateUrl: 'email.component.html'
})
export class EmailComponent {
    public to:string="";
    @Output() OpenDialogMail = new EventEmitter();

    @Input()
    private emailontologia: emailModel;

    public opened: boolean = true;

    setTo(toMail:string){
        this.to=toMail;
    }
    public open(event) {
        this.OpenDialogMail.emit({open:this.opened,to:this.to});
    }
}