# Aurum

Aplikacja mobilna sklepu z odzieżą stworzona w **Kotlinie** przy użyciu **Jetpack Compose** oraz backendu **Node.js/Express** z bazą danych **PostgreSQL**. Aurum umożliwia przeglądanie produktów, rejestrację i logowanie, zarządzanie koszykiem oraz wishlistą — wszystko w nowoczesnym, responsywnym interfejsie.

## Przegląd

Aurum to pełnostackowa aplikacja mobilna, która przenosi doświadczenie zakupów prosto na Twój telefon. Oferuje:

- **Katalog produktów**: Przeglądaj i oglądaj szczegółowe informacje o każdym artykule odzieżowym.  
- **Autoryzacja użytkowników**: Bezpieczna rejestracja i logowanie oparte na JWT.  
- **Koszyk**: Dodawaj produkty z wybranym rozmiarem i kolorem, przeglądaj zawartość koszyka i usuwaj elementy.  
- **Wishlist**: Zapisuj ulubione produkty, aby wrócić do nich później.  

## Funkcje

- **Przeglądanie produktów**: Pobieranie i wyświetlanie listy produktów z serwera.  
- **Rejestracja i logowanie**: Bezpieczne endpointy do tworzenia kont i uzyskiwania tokenów JWT.  
- **Zarządzanie koszykiem**: Dodawanie produktów, przeglądanie zawartości koszyka i usuwanie przedmiotów.  
- **Zarządzanie wishlistą**: Dodawanie i usuwanie produktów z osobistej listy życzeń.  

## Stos technologiczny

- **Aplikacja Android**:
  - Kotlin  
  - Jetpack Compose  
  - Retrofit + OkHttp + Gson  
  - Coroutines (funkcje suspend)  

- **Backend**:
  - Node.js + Express  
  - CORS i middleware JSON  
  - Autoryzacja JWT (jsonwebtoken)  
  - PostgreSQL (node-postgres)  
  - Bcrypt do hashowania haseł  

## Architektura

- **Wzorzec repozytorium**: `AuthRepository`, `CartRepository` itd. enkapsulują logikę wywołań API.  
- **Instancje API**: Singletony `Retrofit` (`AurumApi`, `CartApiInstance`, `WishlistApiInstance`) odpowiadają za warstwę sieciową.  
- **Obiekty transferu danych**: `ProductDto`, `CartItemDto`, `WishlistItemDto` mapują dane JSON.  

## Punkty końcowe API

| Metoda | Endpoint                    | Opis                                     |
| ------ | --------------------------- | ---------------------------------------- |
| GET    | `/api/products`             | Pobierz wszystkie produkty               |
| POST   | `/register`                 | Utwórz nowe konto                        |
| POST   | `/login`                    | Zaloguj się i otrzymaj JWT               |
| GET    | `/api/cart`                 | Pobierz zawartość koszyka (autoryzacja)  |
| POST   | `/api/cart`                 | Dodaj przedmiot do koszyka (autoryzacja) |
| DELETE | `/api/cart/:cartItemId`     | Usuń przedmiot z koszyka (autoryzacja)   |
| GET    | `/api/wishlist`             | Pobierz listę życzeń (autoryzacja)       |
| POST   | `/api/wishlist`             | Dodaj przedmiot do życzeń (autoryzacja)  |
| DELETE | `/api/wishlist/:itemId`     | Usuń przedmiot z życzeń (autoryzacja)    |

## Wkład

Wkłady są mile widziane! Zachęcamy do otwierania issue lub pull requestów dotyczących poprawek, ulepszeń lub nowych funkcji.

## Licencja

Projekt jest udostępniony na licencji MIT. Dodaj plik [LICENSE](LICENSE) ze szczegółami.
