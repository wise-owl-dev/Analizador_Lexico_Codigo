# Analizador Léxico para Java y Robot

Este proyecto implementa un analizador léxico con interfaz gráfica que permite analizar código en dos lenguajes diferentes:
- Java: Analizador completo para código Java
- Robot: Analizador especializado para un lenguaje de control de brazos robóticos

## 📋 Tabla de Contenidos

- [Descripción](#Descripción)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Características](#características)
- [Requisitos](#requisitos)
- [Instalación y Ejecución](#instalación-y-ejecución)
- [Uso](#uso)
- [Tipos de Tokens](#tipos-de-tokens)
- [Ejemplos](#Ejemplos)

## 📝 Descripción

El analizador léxico es una herramienta educativa que analiza el texto de entrada y lo descompone en "tokens" o unidades léxicas como palabras clave, identificadores, operadores, etc. Este proyecto implementa dos analizadores específicos:

1. **Analizador Java**: Reconoce tokens estándar del lenguaje Java, incluyendo palabras clave, identificadores, operadores, literales y comentarios.

2. **Analizador Robot**: Diseñado para un lenguaje especializado de control de brazos robóticos, con comandos específicos como `iniciar()`, `velocidad=50`, etc.

La aplicación proporciona una interfaz gráfica que permite visualizar los tokens identificados y gestionar la tabla de símbolos.

## 🔍 Estructura del Proyecto

El proyecto está compuesto por tres clases principales:

- **AnalizadorLexicoGUI.java**: Interfaz gráfica de usuario que permite interactuar con los analizadores.
- **AnalizadorJava.java**: Implementa el analizador léxico para código Java.
- **AnalizadorRobot.java**: Implementa el analizador léxico para el lenguaje de control de robots.

## ✨ Características

- **Interfaz gráfica intuitiva** con área de código fuente y visualización de resultados
- **Numeración de líneas** en el editor de código
- **Tabla de tokens** con información detallada
- **Tabla de símbolos** que registra identificadores encontrados
- **Ejemplos predefinidos** para facilitar el aprendizaje
- **Reconocimiento de múltiples tipos de tokens** específicos para cada lenguaje
- **Soporte para comentarios** (en Java)
- **Detección de métodos con parámetros** (en Robot)

## 📋 Requisitos

- Java Development Kit (JDK) 8 o superior
- Visual Studio Code
- Extensión "Extension Pack for Java" para VS Code (recomendado)

## 🚀 Instalación y Ejecución

### Usando Visual Studio Code

1. Clone o descargue este repositorio
2. Abra la carpeta del proyecto en Visual Studio Code
3. Asegúrese de tener instalada la extensión "Extension Pack for Java"
4. Abra la clase `AnalizadorLexicoGUI.java`
5. Haga clic en el botón "Run" (▶️) que aparece en la parte superior derecha del editor
   
Alternativamente, puede usar el terminal integrado de VS Code:

## 📌 Uso

1. Seleccione el tipo de análisis que desea realizar (Robot o Java) en el menú desplegable.
2. Ingrese el código a analizar en el área de texto o cargue un ejemplo usando los botones disponibles.
3. Haga clic en el botón "Analizar" para procesar el código.
4. Revise los resultados en las tablas de tokens y símbolos.
5. Use el botón "Limpiar" para borrar todo y comenzar un nuevo análisis.

## 🔖 Tipos de Tokens

### Analizador Java

- **PALABRA_CLAVE**: Palabras reservadas de Java (if, while, class, etc.)
- **IDENTIFICADOR**: Nombres de variables, métodos, clases, etc.
- **OPERADOR**: Operadores aritméticos, lógicos, relacionales, etc.
- **DELIMITADOR**: Caracteres que delimitan estructuras (paréntesis, llaves, etc.)
- **LITERAL_STRING**: Cadenas de texto entre comillas dobles
- **LITERAL_CHAR**: Caracteres entre comillas simples
- **LITERAL_NUM**: Valores numéricos enteros o decimales
- **COMENTARIO**: Comentarios de una línea (//) o multilinea (/* */)
- **DESCONOCIDO**: Tokens no reconocidos

### Analizador Robot

- **PALABRA_R**: La palabra "Robot"
- **IDENTIFICADOR**: Nombres de robots (r1, etc.)
- **ACCION**: Comandos como iniciar, finalizar, cerrarGarra, abrirGarra
- **METODO**: Propiedades del robot como base, cuerpo, garra, velocidad
- **NUMERO**: Valores numéricos
- **IGUAL**: Operador de asignación (=)
- **PARENTESIS_IZQ/DER**: Paréntesis izquierdo/derecho
- **LLAVE_IZQ/DER**: Llaves izquierda/derecha
- **PUNTO**: Operador de acceso a métodos y propiedades
- **DESCONOCIDO**: Tokens no reconocidos

## 📝 Ejemplos

### Ejemplo de código Java:

```java
public class Ejemplo {
    public static void main(String[] args) {
        int x = 10;
        double y = 3.14;
        // Este es un comentario
        if (x > 5) {
            x++;
        } else {
            x--;
        }
        /* Comentario
           multilinea */
    }
}
```

### Ejemplo de código Robot:

```
Robot r1
r1.iniciar()
r1.velocidad=50
r1.base=180
r1.cuerpo=45
r1.garra=90
r1.cerrarGarra()
r1.abrirGarra()
r1.finalizar()
```

## 👥 Autores

Delgado Molina Karla Rocío
Martínez Martínez Jesús Alexander
Roque Hernández Diego Misael

## 🛠️ Entorno de Desarrollo

Este proyecto ha sido desarrollado y probado en Visual Studio Code con las siguientes configuraciones:

- Visual Studio Code
- Extension Pack for Java que incluye:
  - Language Support for Java
  - Debugger for Java
  - Test Runner for Java
  - Maven for Java
  - Project Manager for Java
  - Visual Studio IntelliCode

---

📌 **Nota**: Este analizador léxico es un proyecto educativo y puede no cubrir todas las características y complejidades de los lenguajes de programación completos.
