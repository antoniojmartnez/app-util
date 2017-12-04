/**
 * Extiende la clase HashMap de Java y le añade algunas funcionalidades extra
 *
 * @param <K> Tipo de las claves
 * @param <V> Tipo de los valores
 */
public class HashMap<K,V> extends java.util.LinkedHashMap<K,V> {
    /**
     * Crea un nuevo HashMap con las claves y los valores intercambiados
     *
     * @return HaspMap resultante
     */
    public java.util.LinkedHashMap<K,V> swapKeysValues() {
        java.util.LinkedHashMap<K,V> rev = new java.util.LinkedHashMap<K,V>();
        for(Entry entry : this.entrySet())
            rev.put((K) entry.getValue(), (V) entry.getKey());
        return rev;
    }
}
