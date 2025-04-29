public class JavaVersionTest {
	public static void main(String args[]) {
		String javaVersion = System.getProperty("java.version");
		String javaRuntimeVersion = System.getProperty("java.runtime.version");
		
		System.out.println(javaVersion);
		System.out.println(javaRuntimeVersion);
	}
}
