import {Component, OnInit, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements  OnInit {
  title = 'Team 1 IEF Stocks';
  companyInfo = null;
  companyFD = null;
  ngOnInit(): void {
    console.log('INIT');
  }

  getCompanyInfo(company): void{
    this.companyInfo = company;
    console.log('CompanyInfo',  this.companyInfo);
  }
  getDateFilter(company): void{
    this.companyFD = company;
    console.log('DateFilter', this.companyFD);
  }
}
