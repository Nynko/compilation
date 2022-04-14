
int func(int a,int b,int c){
	int d;
	int e;
	int f;
	int g;
	d = a + b + c;
	e = 3;
	f = 4;
	g = 5;
	if (d>=0) {
		if(a<=0){
			print(d);
			return e;
		}
		if(b<=0){
			print(d);
			return f;
		}

		if(c<=0){
			print(d);
			f = 0;
			e = 1;
			g = 2;
			// return g;
		}
	}
	else{
		return func(a+1,b+1,c+1);
	}

	print(f);
	print(e);
	print(g);
	return 2001;
}


int main(){
	int a;
	a = func(2,3,-3); // 2,3,-3 --> c <= 0 --> d = 2 et f=0 et e=1
	print(a);
	return 0;

}
