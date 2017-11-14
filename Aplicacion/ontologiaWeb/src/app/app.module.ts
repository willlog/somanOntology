import { NgModule } from '@angular/core';
import { BrowserModule }  from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpModule, JsonpModule } from '@angular/http';
import { RouterModule, Routes } from '@angular/router';

import { AppRoutingModule } from './app.routing.module';
//Para Categorias
import { AppComponent } from './app.component';

//Para estilo de la pagina

import { SomanModule }  from './soman/soman.module';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule, 
    JsonpModule,
    AppRoutingModule,
    SomanModule
  ],
  exports: 
  [
    RouterModule
  ],
  declarations: [
    AppComponent
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
