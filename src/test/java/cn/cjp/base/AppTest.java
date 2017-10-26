package cn.cjp.base;

import java.nio.charset.Charset;

import org.apache.log4j.Logger;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.hash.PrimitiveSink;

public class AppTest {

	private static final Logger logger = Logger.getLogger(AppTest.class);

	public static void main(String[] args) {

		Funnel<User> funnel = new Funnel<User>() {

			private static final long serialVersionUID = 4606951192912715901L;

			@Override
			public void funnel(User from, PrimitiveSink into) {
				into.putString(from.username, Charset.forName("UTF-8"));
			}
		};

		for (int i = 0; i < 100; i++) {
			User user = new User();
			user.username = "Sucre" + i;

			HashFunction hashFunc = Hashing.murmur3_128();
			// Hasher hasher = hashFunc.newHasher(30000);
			HashCode hashCode = hashFunc.hashObject(user, funnel);
			long value = hashCode.asLong();
			System.out.println(value);
		}

		BloomFilter<User> filter = BloomFilter.create(funnel, 1_000_000);

		for (int i = 0; i < 100; i++) {
			User user = new User();
			user.username = "Sucre" + i;

			System.out.println(filter.mightContain(user));
			filter.put(user);
		}

	}

}

class User {
	String username;

	int age;
}
