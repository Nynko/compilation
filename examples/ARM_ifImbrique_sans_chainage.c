
int func(int a,int b,int c){

	if (a==0) {
		if(b==0) {
			if(c==0) {
				return 0;
			}
			return func(a,b,c-1) + 1;
		}
		return func(a,b-1,c) + 1;
	}
	else{
		return func(a-1,b,c) + 1;
	}
	
}


int main(){
	int a;
	a = func(10,2,200);
	print(a);
	return 0;

}
