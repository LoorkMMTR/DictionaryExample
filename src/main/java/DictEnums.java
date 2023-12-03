import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class DictEnums {

    @RequiredArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum DictLang {
        RUSSIAN("rusLang.txt"),
        NUMERIC("numericLang.txt"),
        LATIN("latinLang.txt");

        @Getter
        String fileName;

        @Override
        public String toString() {
            return "src/main/resources/" + this.fileName;
        }

    }

    enum DictOption {
        help,
        create,
        edit,
        delete,
        search,
        view
    }

}
