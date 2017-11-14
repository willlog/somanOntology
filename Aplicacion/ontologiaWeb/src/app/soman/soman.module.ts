import { NgModule}       from '@angular/core';
import { CommonModule }   from '@angular/common';
import { FormsModule }    from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

import { SomanComponent } from './soman.component';

import { CategoryComponent } from './category.component';
import { CategoryService } from './category.service';

import { Ontologia } from './ontologia.model';
import { OntologiaComponent } from './ontologia.component';
import { OntologiaService } from './ontologia.service';



//ontologia for fields headers

import { isDescribedBy } from './isDescribedBy.model';
import { isRelatedBy } from './isRelatedBy.model';

//set data for model call

import { dataModel } from './dataModel.model';

import { SomanRoutingModule } from './soman.routing.module';

import { OntoimageComponent } from './ontoimage.component';
import { LinkComponent } from './link.component';
import { DataComponent } from './data.component';
import { ReadComponent} from './read.component';
import { ItemdataComponent} from './itemdata.component';

//for map

import { AgmCoreModule } from '@agm/core';
import { MapaComponent } from './mapa.component';
import { EmailComponent } from './email.component';


// en kendo grid
import { GridModule } from '@progress/kendo-angular-grid';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { InputsModule } from '@progress/kendo-angular-inputs';
import { DialogModule } from '@progress/kendo-angular-dialog';
import { ButtonModule,ButtonGroupModule,DropDownButtonModule,SplitButtonModule } from '@progress/kendo-angular-buttons';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    SomanRoutingModule,
    BrowserModule, 
    BrowserAnimationsModule, 
    DialogModule,
    GridModule,
    InputsModule,
    ButtonModule,
    ButtonGroupModule,
    DropDownButtonModule,
    SplitButtonModule,
    AgmCoreModule.forRoot({apiKey:'AIzaSyCVwRbLwfHrNQIGzEEuyd7KaSiEfo9qoQg'})
  ],
  declarations: [
    CategoryComponent,
    OntologiaComponent,
    OntoimageComponent,
    SomanComponent,
    LinkComponent,
    DataComponent,
    MapaComponent,
    ReadComponent,
    ItemdataComponent,
    EmailComponent
  ],
  providers: [  
    CategoryService,
    OntologiaService
  ]
})
export class SomanModule {}