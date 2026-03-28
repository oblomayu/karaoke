# Компиляция Java файлов
all:
	javac -d . src/*.java

# Запуск программы
run:
	java karaokeServer

# Очистка скомпилированных файлов
clean:
	rm -f *.class
