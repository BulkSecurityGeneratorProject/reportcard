(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('categorie', {
            parent: 'entity',
            url: '/categorie?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Categories'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/categorie/categories.html',
                    controller: 'CategorieController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('categorie-detail', {
            parent: 'entity',
            url: '/categorie/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Categorie'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/categorie/categorie-detail.html',
                    controller: 'CategorieDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Categorie', function($stateParams, Categorie) {
                    return Categorie.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'categorie',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('categorie-detail.edit', {
            parent: 'categorie-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/categorie/categorie-dialog.html',
                    controller: 'CategorieDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Categorie', function(Categorie) {
                            return Categorie.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('categorie.new', {
            parent: 'categorie',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/categorie/categorie-dialog.html',
                    controller: 'CategorieDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                libelle: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('categorie', null, { reload: 'categorie' });
                }, function() {
                    $state.go('categorie');
                });
            }]
        })
        .state('categorie.edit', {
            parent: 'categorie',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/categorie/categorie-dialog.html',
                    controller: 'CategorieDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Categorie', function(Categorie) {
                            return Categorie.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('categorie', null, { reload: 'categorie' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('categorie.delete', {
            parent: 'categorie',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/categorie/categorie-delete-dialog.html',
                    controller: 'CategorieDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Categorie', function(Categorie) {
                            return Categorie.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('categorie', null, { reload: 'categorie' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
