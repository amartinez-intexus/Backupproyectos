void main() {

  print( greetEveryone() );
  
  print( 'Suma: ${ addTwoNumbers( 10, 20 ) }' );
  
  print( greetPerson( name: 'Fernando', message: 'Hi,' ) );
  
}


String greetEveryone() => 'Hello everyone!';

int addTwoNumbers( int a, int b ) => a + b;
 /* La notacion que tiene puesta la variable b es para indicar que es posible que 
  * encuentre este valor como vacio o en tal caso se le indice un valor por defecto
  *si no llega a encontrar ningun valor   [ int b = 0 ]
  *Otra manera de manejarlo es indicando que puede ser dinamico   int? b
  */
int addTwoNumbersOptional( int a, [ int b = 0 ]) {
//   b ??= 0;
  return a + b;
}
/*
*la palabra reservada required indica que debe ser obligarotio el envio del una variable
*aunque las demas no sean necesarias por el hecho de estar en llaves {} ya que
*pueden ser usadas con un valor por defecto asignado 
*/

String greetPerson({ required String name, String message = 'Hola,' }) {
  
  return '$message Fernando';
}









