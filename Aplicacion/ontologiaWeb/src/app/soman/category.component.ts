import { Component, Input, Output, EventEmitter } from '@angular/core';

import { Category } from './category.model';

@Component({
    selector: 'category',
    templateUrl: './category.component.html'
})
export class CategoryComponent {

    @Input()
    private category: Category;
}