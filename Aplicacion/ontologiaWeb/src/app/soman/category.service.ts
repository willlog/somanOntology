import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Globals} from './global';
import 'rxjs/Rx';

import { Category } from './category.model';



@Injectable()
export class CategoryService {
    
    

    constructor(private http: Http) { }

    getCategorys() {
        return this.http.get(Globals.BASE_URL_API_REST+'Category')
            .map(response => response.json())
            .catch(error => this.handleError(error));
    }

   
    private handleError(error: any) {
        console.error(error);
        return Observable.throw("Server error (" + error.status + "): " + error.text())
    }
}