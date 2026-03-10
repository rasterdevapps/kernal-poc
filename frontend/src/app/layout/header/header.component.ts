import { Component, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  readonly tcodeSubmitted = output<string>();
  readonly tcodeInput = signal('');

  onInput(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.tcodeInput.set(value);
  }

  onTCodeSubmit(): void {
    const code = this.tcodeInput().trim().toUpperCase();
    if (code) {
      this.tcodeSubmitted.emit(code);
      this.tcodeInput.set('');
    }
  }

  onKeyDown(event: KeyboardEvent): void {
    if (event.key === 'Enter') {
      this.onTCodeSubmit();
    }
  }
}
