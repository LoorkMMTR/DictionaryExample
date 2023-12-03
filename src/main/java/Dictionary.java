import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dictionary {

    private File dictionaryDirectory; // Хранит путь к папке со словарями
    private File dictionaryFile; // Хранит путь к файлу с выбранным словарем
    private File source; // Хранит путь к файлу, который нужно переводить
    private File dest; // Хранит путь к файлу, в который нужно записывать перевод
    private Map<String, String> dictionary; // Хранит выбранный словарь

    private File langDictionary;
    private final File rusDictionary = new File("src/main/resources/rusLang.txt");
    private Map<String, ArrayList<String>> tempDictionary;
    private static final Scanner sc = new Scanner(System.in);


    private static void startConsoleWorker() {
        System.out.println("Let's start? (Y/N)");
        if (sc.nextLine().equals("N")) {
            System.out.println("Bye. Good luck");
            System.exit(0);
        } else {
            System.out.println("Input option");
            applyOptionActions(sc.nextLine());
        }
    }

    private static void applyOptionActions(String inputOption) {

        switch (inputOption) {
            case "help" -> helpOption();
            case "create" -> System.out.println("creating new entry");
            case "edit" -> System.out.println("editing current entry");
            case "delete" -> System.out.println("deleting current entry");
            case "search" -> System.out.println("searching value bu key");
            case "view" -> viewAllDictionaries();
            default -> {
                System.out.println("Incorrect option value\nInput option");
                applyOptionActions(sc.nextLine());
            }
        }
        System.out.println("Input option");

    }

    private static void helpOption() {
        var options = new ArrayList<>(Arrays.asList(DictEnums.DictOption.values()));
        System.out.println("Available options: \n" + options + "\nInput option");
        applyOptionActions(sc.nextLine());
    }

    private static void viewAllDictionaries() {
        System.out.println("output all dict");
        applyOptionActions(sc.nextLine());
    }




        public void Dictionary (DictEnums.DictLang lang){

            if (lang.equals(DictEnums.DictLang.LATIN)) {
//           String valuesStream =  Arrays.stream(Objects.requireNonNull(new File(DictLang.LATIN.getFilePath()).list()));
//           Stream<ArrayList<String>> translationsStream = Arrays.stream(new File(DictLang.RUSSIAN.getFilePath()).list());
//
//            tempDictionary.putAll(new File("src/main/resources/latinLang.txt").list());
            }


        }






        /*Конструктор без параметров. Единственный доступный.
         * Вызывает private метод consoleMenu(), который запускает цепочку
         * вызовов методов для инициализации полей и перевода*/
    public Dictionary() throws UnsupportedTranslationException {
            consoleMenu();
        }


        public void writeFileNio (File file, String content){
            try {
                Files.writeString(Paths.get(file.toURI()), content, StandardOpenOption.CREATE);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static void readRusDict () throws IOException {
            var bufferedReader = new BufferedReader(new FileReader("src/main/resources/rusLang.txt"));
            while (bufferedReader.ready()) System.out.println(bufferedReader.readLine());
        }



        /*Метод получает на вход 3 строки - пути к файлам:
         * dictionaryDirectory
         * source
         * dest
         * и инициализирует их. Так же инициализирует поле dictionary пустой HashMap.
         * Метод кидает исключение IllegalArgumentException если:
         * dictionaryDirectory не является директорией или не создан;
         * source не создан или является директорией;
         * dest является директорией*/
        private void setTranslator (String dictionaryDirectory, String source, String dest) throws
        IllegalArgumentException {
            this.dictionaryDirectory = new File(dictionaryDirectory);
            if (!this.dictionaryDirectory.isDirectory() || !this.dictionaryDirectory.exists()) {
                throw new IllegalArgumentException("Папка со словарями не является директорией или не создана");
            }
            this.source = new File(source);
            if (!this.source.exists() || this.source.isDirectory()) {
                throw new IllegalArgumentException("Файл с текстом для перевода не создан или является директорией");
            }
            this.dest = new File(dest);
            if (this.dest.isDirectory()) {
                throw new IllegalArgumentException("Файл для результата перевода является директорией");
            }

            this.dictionary = new HashMap<>();
        }

        /*Метод предоставляет пользователю консольное меню, в котором запрашивает:
         * - Путь к папке со словарями;
         * - Путь к файлу, который нужно перевожить;
         * - Путь к файлу, в который нужно записать результат перевода;
         * Метод вызывает setTranslator для инициализации соответствующих полей.
         * Если инициализация прошла успешно, метод обходит папку со словарями и
         * предлагает пользователю выбрать один из словарей для перевода
         * посредством указания его номера.
         * Метод кидает UnsupportedTranslationException, если номер словаря отсутствует
         * в предложенном списке.
         * При успешном выборе словаря, метод инициализирует поле dictionaryFile и
         * вызывает метод setDictionary() для заполнения поля dictionary*/
        private void consoleMenu () throws UnsupportedTranslationException {
            String dictionaryDirectory;
            String source;
            String dest;

            // Запрашиваем у пользователя пути к нужным файлам и директориям
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                System.out.println("==================================================");
                System.out.println("Вас приветствует Ваш персональный переводчик!  ");
                System.out.println("Введите путь к папке со словарями:");
                dictionaryDirectory = reader.readLine();
                System.out.println("Введите путь к файлу с текстом для перевода:");
                source = reader.readLine();
                System.out.println("Введите путь к файлу с результатом перевода:");
                dest = reader.readLine();
                setTranslator(dictionaryDirectory, source, dest);
                System.out.println("==================================================");
                System.out.println();
                System.out.println("Укажите языки перевода:  ");

                // Получаем список файлов в директории dictionaryDirectory
                File[] files = this.dictionaryDirectory.listFiles();

                // Выводим пользователю список имен файлов в dictionaryDirectory для выбора нужного словаря
                for (int i = 0; i < files.length; i++) {
                    System.out.println((i + 1) + ". " + files[i].getName().substring(0, files[i].getName().lastIndexOf('.')));
                }

                System.out.println();
                System.out.println("==================================================");

                // Получаем номер нужного словаря. Если номер не правильный, кидаем UnsupportedTranslationException
                int choice = Integer.parseInt(reader.readLine());
                if (choice < 1 || choice > files.length) {
                    System.out.println("Такого пункта нет в меню! Прощайте!");
                    throw new UnsupportedTranslationException();
                }

                // Инициализируем поле dictionaryFile выбранным пользователем файлом
                this.dictionaryFile = files[choice - 1];

            } catch (IOException e) {
                e.printStackTrace();
            }

            // Вызываем метод для заполнения dictionary
            setDictionary();
        }

        /*Метод считывает данный из файла-словаря и заполняет отображение dictionary, где
         * ключом является слово-оригинал, а значением - его перевод.*/
        private void setDictionary () {
            StringBuilder data = new StringBuilder("");

            // Считываем данные из файла-словаря в StringBuilder
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.dictionaryFile), "windows-1251"))) {
                while (reader.ready()) {
                    data.append(reader.readLine());
                    data.append("\n\r");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Разбиваем считанные данные по строкам
            String[] strings = data.toString().split("\\n+\\r+");
            // Заполняем отображене dictionary полученными данными
            for (String string : strings) {
                this.dictionary.put(string.substring(0, string.indexOf(';')), string.substring(string.lastIndexOf(';') + 1));
            }

            // Вызывает метод для перевода текста
            translate();
        }

        /*Метод считывает данные из файла с текстом для перевода (source), проводит поиск слов
         *в тексте и, если данное слово есть в словаре dictionary, заменяет его на его перевод.
         *Переведенный текст выводится в консоль и записывается в файл dest.*/
        private void translate () {
            StringBuilder data = new StringBuilder("");

            // Читаем текст для перевода из файла source и записываем его в StringBuilder
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.source), "windows-1251"))) {
                while (reader.ready()) {
                    data.append(reader.readLine());
                    data.append("\r\n");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String result = data.toString();
            // pattern находит в тексте все слова
            Pattern pattern = Pattern.compile("(\\w+-\\w+'\\w+)|(\\w+'\\w+)|(\\w+)");
            Matcher matcher = pattern.matcher(result);

            // Проходим по словам в тексте
            while (matcher.find()) {
                // Каждое найденное слово записываем в String
                String s = matcher.group();

                // Если найденное слово содержится в словаре
                if (this.dictionary.containsKey(s.toLowerCase())) {
                    /* Формируем новый pattern посредством StringBuilder с найденным словом.
                     * pattern позволяет найти слово в тексте целиком, чтобы избежать
                     * замены частей слов (например cold = cстарый)*/
                    StringBuilder builder = new StringBuilder("");
                    builder.append("\\b");
                    builder.append(s);
                    builder.append("\\b");

                    Pattern pattern1 = Pattern.compile(builder.toString());
                    Matcher matcher1 = pattern1.matcher(result);
                    // Заменяем все найденные в тексте слова на их перевод из dictionary
                    while (matcher1.find()) {
                        result = matcher1.replaceAll(this.dictionary.get(s.toLowerCase()));
                    }
                }
            }

            // Выводим переведенный текст на экран
            System.out.println();
            System.out.println("==================================================");
            System.out.println("Ваш переведенный текст:");
            System.out.println();
            System.out.println(result);

            // Записываем переведенный текст в файл dest
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.dest)))) {
                writer.write(result);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public static void main (String[]args) throws UnsupportedTranslationException, IOException {
            startConsoleWorker();
//        readRusDict();
        }


}



