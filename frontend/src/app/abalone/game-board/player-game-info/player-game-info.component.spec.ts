import {ComponentFixture, TestBed} from '@angular/core/testing';

import {PlayerGameInfoComponent} from './player-game-info.component';

describe('PlayerGameInfoComponent', () => {
  let component: PlayerGameInfoComponent;
  let fixture: ComponentFixture<PlayerGameInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlayerGameInfoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlayerGameInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
