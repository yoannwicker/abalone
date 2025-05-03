import {Component, input, output} from '@angular/core';
import {NgIf} from "@angular/common";
import {Square} from "../model/square";

@Component({
  selector: 'app-square',
  standalone: true,
  imports: [
    NgIf
  ],
  templateUrl: './square.component.html',
  styleUrl: './square.component.css'
})
export class SquareComponent {
  square = input.required<Square>();
  inLastMovedPositions = input.required<boolean>();
  hoveredDirection = input<string | null>();
  moveDirection = output<string>();
  hoverDirection = output<string | null>();
}
