import {Component, OnInit} from '@angular/core';
import {SearchService} from "./search.service";
import {ErrorHandlerService} from "../error-handler.service";

@Component({
  standalone: true,
  imports: [],
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  serverValue: string = '';

  constructor(private searchService: SearchService, private errorHandlerService: ErrorHandlerService) {}

  ngOnInit() {
    this.searchService.search().subscribe(
      data => {
        this.serverValue = data.message;
      },
      error => {
        alert('Search failed: ' + this.errorHandlerService.handle(error));
      }
    );
  }
}
